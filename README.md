
# Game Store API
![Game Store Logo](img/../assets/images/GameStore.png)

## Description
Restful API for a game store. It contains CRUD operations for Products and a implementation of Shopping Carts with the following operations:

- Create cart
- Get all carts
- Get a cart with it's products sorted by price (lowest to highest)
- Get a cart with it's products sorted by name (alphabetical order)
- Get a cart with it's products sorted by score (highest to lowest)
- Add a product to a cart
- Remove a product from a cart
- Checkout a cart (change it's status to CLOSED and products can't be added/removed to/from it anymore)
- Delete a cart

Spring Boot it's the most popular Java framework to build web applications and was chosen to this project cause of it's three core capabilities:

- Autoconfiguration
- Opinionated approach to configuration
- Ability to create standalone applications


## Technologies used
- Java 11
- Spring Boot
- Spring Data JPA
- Spring Web
- Maven
- Lombok
- H2 Database


## To run the application

To run the application you have two options:

- Once you are on the project folder run the following commands:
```
./mvnw clean install
```
```
java -jar target/Supera-GameStore-API-1.0.jar
```
- Run directly throught the Spring Boot Dashboard on your IDE of choice.

## To run the application

To run the tests:
```
./mvnw clean test
```

## Packages

```
├───.mvn
│	└───wrapper
└───src
	├───main
	│	└───java
	│		├───br
	│		│	└───com
	│		│		└───superaGameStore
	│		│			├───controller
	│		│			│	└───exception
	│		│			├───model
	│		│			├───repository
	│		│			└───service
	│		│				└───impl
	│		└───resources
	└───test
		└───java
			├───br
			│	└───com
			│		└───superaGameStore
			│			├───controller
			│			└───service
			│			
			└───resources
```	

## Models

- ### `Cart`
|   Attribute  |        Type       |
|:------------:|:-----------------:|
|      id      |        long       |
|    status    |       String      |
| cartProducts | List\<CartProduct\> |

- ### `CartProduct`
| Attribute |      Type      |
|:---------:|:--------------:|
|    cpk    | CartProductKey |
|  quantity |       int      |

- ### `CartProductKey`
| Attribute |   Type  |
|:---------:|:-------:|
|    cart   |   Cart  |
|  product  | Product |

- ### `Product`
| Attribute |    Type    |
|:---------:|:----------:|
|     id    |    long    |
|    name   |   String   |
|   image   |   String   |
|   score   |    short   |
|   price   | BigDecimal |


## Product Requests

### `GetAllProducts`

- `GET` `localhost:8080/products/get`: gets all products.
- Response example:

**`200 OK`**
```json
[
	{
		"id": 1,
		"name": "Super Mario Odyssey",
		"image": "super-mario-odyssey.png",
		"score": 100,
		"price": 197.88
	},
	{
		"id": 2,
		"name": "Call Of Duty Infinite Warfare",
		"image": "call-of-duty-infinite-warfare.png",
		"score": 80,
		"price": 49.99
	}
]
```

---
### `GetProduct`

- `GET` `localhost:8080/products/{id}`: gets a specific product by the ID.
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"name": "Super Mario Odyssey",
	"image": "super-mario-odyssey.png",
	"score": 100,
	"price": 197.88
}
```

---
### `PostProduct`

- `POST` `localhost:8080/products/post`: creates a product getting it's attributes throught the JSON body.
- Body example:
```json
{
	"name": "Super Mario Odyssey",
	"image": "super-mario-odyssey.png",
	"score": 100,
	"price": 197.88
}
```
- Response example:

**`201 Created`**
```json
{
	"id": 1,
	"name": "Super Mario Odyssey",
	"image": "super-mario-odyssey.png",
	"score": 100,
	"price": 197.88
}
```

---
### `PutProduct`

- `PUT` `localhost:8080/products/put`: edits a product getting it's attributes throught the JSON body.
- Body example:
```json
{
	"id": 1,
	"name": "Super Mario Odyssey",
	"image": "super-mario-odyssey.png",
	"score": 100,
	"price": 197.88
}
```
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"name": "Super Mario Odyssey",
	"image": "super-mario-odyssey.png",
	"score": 100,
	"price": 197.88
}
```

---
### `DeleteProduct`

- `DELETE` `localhost:8080/products/{id}`: deletes a specific product by the ID.
- Response example: **`204 No Content`**

## Cart Requests

### `CreateCart`

- `POST` `localhost:8080/carts/create`: creates a empty cart.
- Response example:

**`201 Created`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [],
	"totalPrice": 0,
	"subTotalPrice": 0,
	"freight": 0
}
```
---

### `GetAllCarts`

- `GET` `localhost:8080/carts/get`: gets all carts.
- Response example:

**`200 OK`**
```json
[
	{
		"id": 1,
		"status": "OPEN",
		"cartProducts": [
			{
				"quantity": 1,
				"product": {
					"id": 1,
					"name": "Super Mario Odyssey",
					"image": "super-mario-odyssey.png",
					"score": 100,
					"price": 197.88
				}
			}
		],
		"totalPrice": 207.88,
		"freight": 10,
		"subTotalPrice": 197.88
	},
	{
		"id": 2,
		"status": "OPEN",
		"cartProducts": [
			{
				"quantity": 1,
				"product": {
					"id": 2,
					"name": "Call Of Duty Infinite Warfare",
					"image": "call-of-duty-infinite-warfare.png",
					"score": 80,
					"price": 49.99
				}
			}
		],
		"totalPrice": 59.99,
		"freight": 10,
		"subTotalPrice": 49.99
	}
]
```
---

### `GetCartWithProductsByPrice`

- `GET` `localhost:8080/carts/{id}/productsByPrice`: gets a cart by the ID with it’s products sorted by price (lowest to highest).
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [
		{
			"quantity": 1,
			"product": {
				"id": 2,
				"name": "Call Of Duty Infinite Warfare",
				"image": "call-of-duty-infinite-warfare.png",
				"score": 80,
				"price": 49.99
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 3,
				"name": "The Witcher III Wild Hunt",
				"image": "the-witcher-iii-wild-hunt.png",
				"score": 250,
				"price": 119.50
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 1,
				"name": "Super Mario Odyssey",
				"image": "super-mario-odyssey.png",
				"score": 100,
				"price": 197.88
			}
		}
	],
	"totalPrice": 367.37,
	"freight": 0,
	"subTotalPrice": 367.37
}
```
---


### `GetCartWithProductsByName`

- `GET` `localhost:8080/carts/{id}/productsByName`: gets a cart by the ID with it’s products sorted by name (alphabetical order).
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [
		{
			"quantity": 1,
			"product": {
				"id": 2,
				"name": "Call Of Duty Infinite Warfare",
				"image": "call-of-duty-infinite-warfare.png",
				"score": 80,
				"price": 49.99
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 3,
				"name": "Mortal Kombat XL",
				"image": "mortal-kombat-xl.png",
				"score": 150,
				"price": 69.99
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 1,
				"name": "Super Mario Odyssey",
				"image": "super-mario-odyssey.png",
				"score": 100,
				"price": 197.88
			}
		}
	],
	"totalPrice": 317.86,
	"freight": 0,
	"subTotalPrice": 317.86
}
```
---

### `GetCartWithProductsByScore`

- `GET` `localhost:8080/carts/{id}/productsByScore`: gets a cart by the ID with it’s products sorted by score (highest to lowest).
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [
		{
			"quantity": 1,
			"product": {
				"id": 3,
				"name": "Mortal Kombat XL",
				"image": "mortal-kombat-xl.png",
				"score": 150,
				"price": 69.99
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 1,
				"name": "Super Mario Odyssey",
				"image": "super-mario-odyssey.png",
				"score": 100,
				"price": 197.88
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 2,
				"name": "Call Of Duty Infinite Warfare",
				"image": "call-of-duty-infinite-warfare.png",
				"score": 80,
				"price": 49.99
			}
		}
	],
	"totalPrice": 317.86,
	"freight": 0,
	"subTotalPrice": 317.86
}
```
---

### `AddProduct`

- `PUT` `localhost:8080/carts/add`: adds a product to a cart getting cart's ID, product's ID and quantity throught the JSON body.
- Body example:
```json
{
	"cartId": 1,
	"productId": 1,
	"quantity": 1
}
```
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [
		{
			"quantity": 1,
			"product": {
				"id": 2,
				"name": "Shards of Darkness",
				"image": "shards-of-darkness.png",
				"score": 400,
				"price": 71.94
			}
		},
		{
			"quantity": 2,
			"product": {
				"id": 1,
				"name": "Mortal Kombat XL",
				"image": "mortal-kombat-xl.png",
				"score": 150,
				"price": 69.99
			}
		}
	],
	"totalPrice": 241.92,
	"subTotalPrice": 211.92,
	"freight": 30
}
```
---

### `RemoveProduct`

- `PUT` `localhost:8080/carts/remove`: removes a product to a cart getting cart's ID, product's ID and quantity throught the JSON body.
- Body example:
```json
{
	"cartId": 1,
	"productId": 1,
	"quantity": 1
}
```
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [
		{
			"quantity": 1,
			"product": {
				"id": 2,
				"name": "Shards of Darkness",
				"image": "shards-of-darkness.png",
				"score": 400,
				"price": 71.94
			}
		},
		{
			"quantity": 1,
			"product": {
				"id": 1,
				"name": "Mortal Kombat XL",
				"image": "mortal-kombat-xl.png",
				"score": 150,
				"price": 69.99
			}
		}
	],
	"totalPrice": 161.93,
	"subTotalPrice": 141.93,
	"freight": 20
}
```
---

### `CheckOutCart`

- `PUT` `localhost:8080/carts/checkout/{id}`: checkout a specific cart by the ID.
- Response example:

**`200 OK`**
```json
{
	"id": 1,
	"status": "CLOSED",
	"cartProducts": [
		{
			"quantity": 1,
			"product": {
				"id": 1,
				"name": "Super Mario Odyssey",
				"image": "super-mario-odyssey.png",
				"score": 100,
				"price": 197.88
			}
		}
	],
	"subTotalPrice": 197.88,
	"freight": 10,
	"totalPrice": 207.88
}
```
---

### `DeleteCart`

- `DELETE` `localhost:8080/carts/{id}`: deletes a specific cart by the ID.
- Response example: **`200 OK`**

---