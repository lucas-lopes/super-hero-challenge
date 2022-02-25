# Super Hero Challenge

---

This application is an API responsible for doing the CRUD operations related to heroes:

```
POST /api/v1/heroes
GET /api/v1/heroes
GET /api/v1/heroes/{name}
GET /api/v1/heroes/properties?property={property}&value={value}
PUT /api/v1/heroes/{id}
DELETE /api/v1/heroes/{id}
```

PS: There is a section to explain each endpoint in this document.

---

It's an application written in the following technologies:

* Java 11
* Spring Boot
* Spring Data
* Spring Test
* Lombok
* H2 Database
* Docker

---

## How to test?

### Pre-requisite:
* Docker
* Maven

### How to run?

PS: We're using port 9001.

We're using Docker and docker-compose to be easy testing our application.
So, after decompressing or cloning the project:

```bash
$ mvn clean install
$ docker compose up -d
```

### Where can I test?

We have two possibilities:

1 - Using Swagger that can be found in the following address:

[Swagger Super Hero API](http://localhost:9001/swagger-ui.html#/hero-controller)

PS: The application need to be running before hit the link above.

2 - Using the Postman collection shared together this project inside `src/main/resource/postman` folder:

[Postman Collection][postman-collection]

---

## Database

I used H2 database to test the API. It's very simple access and follow your data:

![Postman Collection][h2-picture]

* **Login:** http://localhost:9001/h2-console
* **JDBC URL:** jdbc:h2:mem:superherodb
* **User Name:** sa
* **Password:**

---

## API in Details

### Add new Hero

We can add a new hero informing 
properties like the following example:

**Request example:**

```
POST /api/v1/heroes
```

```json
{
  "name": "Peter Parker",
  "alias": "Spider Man",
  "origin": "Radioactive Spider bite during a science experiment endowed proportionate capabilities of an arachnid.",
  "powers": [
    "intelligence",
    "strength",
    "speed",
    "durability",
    "energy-projection",
    "fighting-skills"
  ],
  "weapons": [
    "synthetic-webbing",
    "shocker-gauntlet",
    "anti-gravity-gun"
  ],
  "associations": [
    "Miguel",
    "Miles"
  ]
}
```

**Response Example:**

```json
{
  "name": "Peter Parker",
  "alias": "Spider Man",
  "origin": "Radioactive Spider bite during a science experiment endowed proportionate capabilities of an arachnid.",
  "powers": [
    "intelligence",
    "strength",
    "speed",
    "durability",
    "energy-projection",
    "fighting-skills"
  ],
  "weapons": [
    "synthetic-webbing",
    "shocker-gauntlet",
    "anti-gravity-gun"
  ],
  "associations": [
    "Miguel",
    "Miles"
  ]
}
```

### Find All Heroes

If you call the resource below, you will receive two heroes that was saved in advance to facilitate 
your tests:

```
GET /api/v1/heroes
```

**Response example:**

```json
[
    {
        "alias": "Captain Marvel",
        "name": "Carol Danvers",
        "origin": "Exposed to Space Stone reactor overload",
        "powers": [
            "photon-blast",
            "flight",
            "super-strength",
            "healing"
        ],
        "weapons": [],
        "associations": [
            "space-stone",
            "skrulls",
            "photon",
            "kree",
            "avengers"
        ]
    },
    {
        "alias": "Iron Man",
        "name": "Tony Stark",
        "origin": "Kidnapped in Afghanistan, created the first iron-man suit to escape.",
        "powers": [
            "genius-intelligence",
            "wealth",
            "flight"
        ],
        "weapons": [
            "arc-reactor",
            "iron-man-suit",
            "iron-legion"
        ],
        "associations": [
            "war-machine",
            "avengers",
            "jarvis",
            "thanos",
            "pepper-potts"
        ]
    }
]
```

### Find Hero By Name

We can find a hero by name:

```
GET /api/v1/heroes/{name}
```

**Request example:**

```
GET /api/v1/heroes/Tony%20Stark
```

**Response example:**

```json
{
    "alias": "Iron Man",
    "name": "Tony Stark",
    "origin": "Kidnapped in Afghanistan, created the first iron-man suit to escape.",
    "powers": [
        "genius-intelligence",
        "wealth",
        "flight"
    ],
    "weapons": [
        "arc-reactor",
        "iron-man-suit",
        "iron-legion"
    ],
    "associations": [
        "war-machine",
        "avengers",
        "jarvis",
        "thanos",
        "pepper-potts"
    ]
}
```

### Find Hero By Property

Properties available:

* power
* weapon
* association

Basically, we can find all heroes who have the property power for example and value 'flight':

```
GET /api/v1/heroes/properties?property={property}&value={value}
```

**Request example:**

```
GET /api/v1/heroes/properties?property=power&value=flight
```

**Response example:**

How can you see below, the heroes Captain Marvel and Iron Main have flight power.

```json
[
  {
    "alias": "Captain Marvel",
    "name": "Carol Danvers",
    "origin": "Exposed to Space Stone reactor overload",
    "powers": [
      "photon-blast",
      "flight",
      "super-strength",
      "healing"
    ],
    "weapons": [],
    "associations": [
      "space-stone",
      "skrulls",
      "photon",
      "kree",
      "avengers"
    ]
  },
  {
    "alias": "Iron Man",
    "name": "Tony Stark",
    "origin": "Kidnapped in Afghanistan, created the first iron-man suit to escape.",
    "powers": [
      "genius-intelligence",
      "wealth",
      "flight"
    ],
    "weapons": [
      "arc-reactor",
      "iron-man-suit",
      "iron-legion"
    ],
    "associations": [
      "war-machine",
      "avengers",
      "jarvis",
      "thanos",
      "pepper-potts"
    ]
  }
]
```

### Update a Hero

You can update one property only like alias for example or all properties. The service already is 
ready to update only the information sent: 

```
PUT /api/v1/heroes/{id}
```

**Request example:**

```
PUT /api/v1/heroes/1
```

```json
{
  "alias": "Powerful Capitan DC"
}
```

**Response example:**

How can you see below, we sent only the alias property and only this property was updated:

```json
{
    "alias": "Powerful Capitan DC",
    "name": "Carol Danvers",
    "origin": "Exposed to Space Stone reactor overload",
    "powers": [
        "photon-blast",
        "flight",
        "super-strength",
        "healing"
    ],
    "weapons": [],
    "associations": [
        "space-stone",
        "skrulls",
        "photon",
        "kree",
        "avengers"
    ]
}
```

### Delete a Hero

```
DELETE /api/v1/heroes/{id}
```

**Request example:**

```
DELETE /api/v1/heroes/1
```

This endpoint doesn't have response body, only a status code 204 if all happened with success.

---

*Thank you for this opportunity. Made with love by Lucas Barbosa.*

[//]: # (These are reference links used in the body of this note.)

[postman-collection]: <src/main/resources/postman/super-hero-challenge.postman_collection.json>
[h2-picture]: <src/main/resources/h2/h2.png>
[h2-login]: <http://localhost:9001/h2-console>
