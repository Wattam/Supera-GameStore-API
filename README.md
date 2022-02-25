# Game Store API
![Game Store Logo](img/../assets/images/GameStore.png)

## Description
Restful API for a game store. It contains CRUD operations for Products and a implementation of Shopping Carts with the following operations:

- Create a empty cart.
- Get all carts.
- Get a cart.
- Get a cart with it's products sorted by name (alphabetical order).
- Get a cart with it's products sorted by price (lowest to highest).
- Get a cart with it's products sorted by score (lowest to highest).
- Add a product to a cart.
- Remove a product from a cart.
- Checkout a cart (change it's status to CLOSED and products can't be added/removed to/from it anymore).
- Delete a cart.

Each cart will have - dinamically calculated - the following values:

- **Subtotal:** the price of all products summed up, without the shipping fee.
- **Total:** the sum of subtotal and shipping fee values.
- **Shipping Fee:** R$ 10,00 for each product added to the cart. If the cart's subtotal value it's equal or higher than R$ 250,00 the shipping fee becomes R$ 0,00.

Spring Boot it's the most popular Java framework to build web applications and was chosen to this project cause of it's three core capabilities:

- Autoconfiguration.
- Opinionated approach to configuration.
- Ability to create standalone applications.


## Technologies used
- Java 11.
- Spring Boot.
- Spring Data JPA.
- Spring Web.
- Maven.
- Lombok.
- H2 Database.


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

## To run the the tests

```
./mvnw clean test
```

## Packages

```
├───.mvn
│	└───wrapper
├───assets
│	└───images
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

### `Index Products`

- `GET` `localhost:8080/products`: index products.
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
### `Show Product`

- `GET` `localhost:8080/products/{id}`: shows a product by the ID.
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
### `Store Product`

- `POST` `localhost:8080/products`: stores a product getting it's attributes throught the JSON body.
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
### `Update Product`

- `PUT` `localhost:8080/products/{id}`: updates a product by the ID getting it's attributes throught the JSON body.
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
### `Delete Product`

- `DELETE` `localhost:8080/products/{id}`: deletes a product by the ID.
- Response example: **`204 No Content`**

## Cart Requests

### `Store Cart`

- `POST` `localhost:8080/carts`: stores a empty cart.
- Response example:

**`201 Created`**
```json
{
	"id": 1,
	"status": "OPEN",
	"cartProducts": [],
	"totalPrice": 0,
	"subTotalPrice": 0,
	"shippingFee": 0
}
```
---

### `Index Carts`

- `GET` `localhost:8080/carts/get`: index carts.
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
		"shippingFee": 10,
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
		"shippingFee": 10,
		"subTotalPrice": 49.99
	}
]
```
---

### `Show Cart`

 - `GET` `localhost:8080/carts/{id}`: shows a cart by the ID.
 - Request parameters can be added to the endpoint to sort the cart products list:
	 - `?sort_by=name`: the products will be sorted by name (alphabetical order).
	 - `?sort_by=price`: the products will be sorted by price (lowest to highest).
	 - `?sort_by=score`: the products will be sorted by score (lowest to highest).

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
		}
	],
	"totalPrice": 367.37,
	"shippingFee": 0.0,
	"subTotalPrice": 367.37
}
```

---

### `AddProduct to Cart`

- `PUT` `localhost:8080/carts/addProduct`: adds a product to a cart getting cart's ID, product's ID and quantity throught the JSON body.
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
	"shippingFee": 30
}
```
---

### `RemoveProduct from Cart`

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
	"shippingFee": 20
}
```
---

### `Checkout Cart`

- `PUT` `localhost:8080/carts/checkout/{id}`: checkouts a cart by the ID.
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
	"shippingFee": 10,
	"totalPrice": 207.88
}
```
---

### `DeleteCart`

- `DELETE` `localhost:8080/carts/{id}`: deletes a cart by the ID.
- Response example: **`200 OK`**

## Additional Information

- Inside `./assets` folder there's a `Products.json` file, that contains products written in `.json` ready to be added throught the `PostProduct` endpoint (even that the file contains more than one product you need to add one at a time).
- There's a bug where the first time a product will be added to a cart throught the `AddProduct to Cart` endpoint it will not appear on response body. The product will be added normally, but just not shown on the response. That happens cause in `Cart.java` model file the fetch type it's setted to `EAGER`.
- A similar bug happens with `RemoveProduct from Cart` endpoint when a product gonna be removed instead of it's quantity reduced: on response body it will show that the product is still there, but it will be removed normally. The cause it's the same of the previous bug, the fetch type on `Cart.java` model.