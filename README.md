# Waterlogged - Back-end

This repository is the back-end component of the Waterlogged project.

The Waterlogged project aims to create a tool allowing white-water kayakers to :

- Find rivers around them that fit their level
- Getting informations about rivers such as their current levels,  their put in, take out, the weather forecast, the number of people who  paddled this river this year
- Get logs about their previous outings
- Get in contact with other paddlers

Back-end provide data to the front-end component from the other APIs. This component runs with SpringBoot/Maven.

## How to run the project ?

### Requirements

To run the project, the followings must be installed on the machine :

- Docker
- Docker-compose

### Run the project

1. Clone the repository

2. Run Docker Compose with the following command

   ```bash
   docker-compose up -d
   ```

3. The API is now available on the port 8081

### Send GraphQL request 

- With Postman : follow this tutorial : https://youtu.be/7pUbezVADQs, using `./src/main/resources/schema.graphqls` to create the schema
- With [GraphQL Playground](https://github.com/prisma/graphql-playground)

Server address : `http://localhost:8081/graphql`

<u>Request examples :</u> 

```yaml
// Get a river by id :
{
    riverById (id: "river-1") {
        id
        name
        level
        difficulty
        latitude
        longitude
    }
} 

// Get a river by location :
{
    riverByLocation(latitude:44.5, longitude:4, radius:50) {
        id
        name
        level
        difficulty
        latitude
        longitude
    }
}
```

## Authors

- [Justin Foltz](https://github.com/JustinFoltz)
- [Maxime Hutinet](https://github.com/maximehutinet)