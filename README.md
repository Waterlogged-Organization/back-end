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

You also need a valid API key used both for Google Geocoding and Google Place services.

### Run the project

1. Clone the repository

2. Add your Google API key :

   1. Duplicate the file `src/main/resources/application.properties.example`

   2. Remove the `.example` extension

   3. Edit the new file (i.e. without extension) to add your Google API key :

      ```properties
      googleAPIKey=<your API key>
      ```

3. Run Docker Compose with the following command in the project root directory

   ```bash
   docker-compose up -d
   ```

4. The API is now available on the port 8081

### Run tests

1. Make sure the image `back-end_back` is built

2. Run the following command into the project root directory

   ```bash
   docker run -it --rm -v "$(pwd)"/.:/backend back-end_back bash -c "cd backend && mvn test"
   ```

### Send GraphQL request 

* With common tools  : `http://localhost:8081/graphql`
* With Grapiql : `http://localhost:8081/graphiql`

<u>Request examples:</u> 

```yaml
// Get a river by id :
{
    getRiverById (id: "river-1") {
        id
        name
        level
    	difficulty
    	coordinate {
            latitude
            longitude
        }
    }
}  

// Get a river by name :
{
    getRiverByName (name: "Verdanson") {
        id
        name
        level
        difficulty
        coordinate {
            latitude
            longitude
        }
    }
} 

// Get rivers contained in an area :
{
    getRiversInArea(latitude:44.5, longitude:4, radius:50) {
        id
        name
        level
        difficulty
        coordinate {
            latitude
            longitude
        }
    }
}

// Get rivers around a place :
{
    getRiversAroundAPlace(name:"Alès", radius:100) {
        id
        name
        level
        difficulty
        coordinate {
            latitude
            longitude
        }
    }
}

// Search rivers and places by pattern :
{
    searchFromPattern(pattern: "La") {
        name
        type
    }
}

```

## Authors 

- [Justin Foltz](https://github.com/JustinFoltz)
- [Maxime Hutinet](https://github.com/maximehutinet)