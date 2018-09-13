

## Design decisions

I have chosen Redis as the persistent store, since it can be scaled out easily. More easily than a relational store,
which would then require a sharded storage architecture. I will be using Spring Data too, so it should be "easy" to
change to a different store anyways.

I have decided to implement a Level 3 RESTful interface, as per the Richardson Maturity Model [1](#references). 
Spring Boot ships with HAL [2](#references) support, which adds hypermedia support to JSON. It is not the most 
beautiful format in the world for human consumption. Neither is it in my humble opinion the best alternative 
(I like Siren better), but it will do the trick, and speed up development a little.  

Even though I am trying to stick to DDD as much as possible I decided to let the @Redis annotations bleed into the 
domain model. I don't think it is a big deal, and getting rid of them would imply more code duplication, and to 
some extent implementing your own custom ORM logic. Handling dirty flags, etc. If I ever decided to get rid of them, 
it would be a matter of creating a new ShortLink class and including the conversion logic in the 
ShortLinkRedisRepository implementation.

I could have relied on the Redis store for the Id generation of ShortLinks. However, they are rather long and also
like having the Id generation strategy as part of the domain model. I think that makes the solution more cohesive.

I have added the Hal-Browser to the project for easier API navigation. http://localhost:9090/hal-browser
You can try to docs links in the Hal-Browser for Api documentation. I like Swagger too, but it is more RESTful Level 2
oriented.

I have decided to combine the User Interface and API project for the sake of brevity. However they would be better
segregated, which would enable us to scale the UI and API layers differently. This would require having a ui-server
side to proxy all outgoing API requests on the client layer. 
This decision has forced me to change the URL used for the short to long URL redirection. Originally the redirection 
was bound to the / of the project, and now is changed to /v. Otherwise the redirection path would conflict with that 
of the User Interface and HAL-browser. I chose /v to keep it short, since it is a shortening service after all, 
otherwise I would have liked /api/shortlink/{id}/visit.

Added Angular Material Design to the UI project to get reactive support easily

I have finally added the number of visits to the ShortLink. Notice that visiting a link should be an operation that
belonged to the ShortLink entity. Regretfully, modifying the counter directly in the Shortlink and then persisting
its changes could lead to phantom reads, and some visits being lost due to concurrency. In order to be safe, the
operation in redis can't be performed like a regular persist operation. It should involve an HINCRBY operation.
I could simply treat the increment of visits llke a regular persist operation, but that could lead to
visits being lost. Maybe that is reasonable. If that was the case I could quickly move the visit operation to 
the ShortLink itself.

Replace domain.utils with real DDD concepts.
Need to set timeouts properly for the RESTful->Redis communication
Need improved look and feel in the link-shortener-ui project

## Building

In order to build the complete project, from the root project folder, type mvn clean install in a command line shell.

Notice that the build process of the link-shortener-ui module could take a while a first time, since it needs to 
download a significant amount of node modules from the internet. (I need to do some housekeeping here, because I feel
it is fetching way too many things). So, don't despair

#### Running the UI Tests

- cd link-shortener-ui
- ng test  

## Running

**Starting up a Redis instance:**

docker run --name some-redis -p 6379:6379 -d redis

**Connecting to the existing Redis instance:**

docker run -it --link some-redis:redis --rm redis redis-cli -h redis -p 6379

If you are running redis from the DockerToolbox in Windows, do not forget
to open the 6379 port in the VirtualBox Network configuration   

**Starting up the Service:**

cd link-shortener-api
 
Then type mvn spring-boot:run in the console. 

Go to http://localhost:9090/ui for the entry point of the solution. 
You can also use the HAL-Browser at http://localhost:9090/hal-browser to navigate the RESTful API and also read 
its documentation

## Known issues

- There is a glitch in the responsiveness of the UI when resizing the browser. The buttons end up slightly misplaced.
  Not centered on the card. Also, the link rows are somewhat spaced further away than normal. Hitting reload solves it
  all.
- For now the UI does not refresh the number of visits periodically. You have to wait for a page reload. Refreshing
  automatically is not that big of a deal anyways.
- Even though the API is Level 3, the client cuts directly to the shortlinks endpoint. This link should be discovered
  by navigating the link relations by their names.  

## References

* [1]: Webber, Parastatidis, Robinson (2010). Web Friendliness and the Richardson Maturity Model.
    In "Rest in Practice, Hypermedia and Systems Architecture" (pp. 18-20). O'Reilly    
* [2]: Kelly, Mike (2013). In HAL, Hypertext Application Language. http://stateless.co/hal_specification.html