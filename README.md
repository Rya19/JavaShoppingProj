# Java Shopping Recommendation System

This project is a Java-based shopping and recommendation system built to practice object-oriented programming, file handling, and basic recommendation logic. It simulates a small e-commerce store where users can search for products, add items to a cart, remove items, and receive product recommendations based on their search history.

## Project Status

This project is a work in progress. The current version includes the main object-oriented structure, an interactive console menu, product types, discounts, cart management, file-based inventory storage, and a simple keyword-based recommendation engine.

## Features

- Interactive console shopping menu
- Physical and digital product types
- Shopping cart system
- Add and remove items from cart
- Search history tracking
- Basic product recommendation system
- Discount support through an interface
- Inventory saving and loading from a text file
- Separate Java class files for cleaner organization

## Technologies Used

- Java
- Object-Oriented Programming
- File I/O
- ArrayLists
- Interfaces
- Inheritance
- Polymorphism
- Console-based user interaction

## File Structure

```text
E_CommerceReccomendEngine.java          # Main program and interactive menu
Item.java                               # Abstract parent class for store items
PhysicalItem.java                       # Physical item subclass with shipping cost
DigitalItem.java                        # Digital item subclass with digital fee
User.java                               # Shopper profile, cart, and search history
StoreManager.java                       # Saves and loads inventory from a file
Discountable.java                       # Interface for discount behavior
Reccomendation_Engine.java              # Keyword-based recommendation logic
COMBINED_E_CommerceReccomendEngine.java # Combined single-file version of the project
```

## How It Works

The program starts by creating sample store items, including physical and digital products. One product is discounted using the `Discountable` interface. The inventory is then saved to a text file and loaded back into the program.

Users can interact with the console menu to:

1. Search for items
2. Add items to their cart
3. View or remove cart items
4. Get product recommendations
5. Checkout and exit

## Recommendation Logic

The recommendation engine uses a simple content-based approach.

It takes the user's search history, breaks it into lowercase keyword tokens, and compares those words against each product description. Products with more matching words receive a higher similarity score and can be recommended to the user.

This is a beginner-friendly version of how recommendation systems can use user behavior and product descriptions to suggest relevant items.

## Object-Oriented Concepts Demonstrated

- **Abstraction:** `Item` is an abstract class used as the foundation for all products.
- **Inheritance:** `PhysicalItem` and `DigitalItem` extend `Item`.
- **Polymorphism:** Different item types implement their own `calculateTotal()` and `ToCSV()` behavior.
- **Encapsulation:** Class fields are kept private or protected and accessed through methods.
- **Interfaces:** `Discountable` defines discount behavior for items.
- **Composition:** `User` contains a shopping cart and search history.
- **File I/O:** `StoreManager` saves and loads inventory data from a file.

## How to Compile

```bash
javac E_CommerceReccomendEngine.java Item.java PhysicalItem.java DigitalItem.java User.java StoreManager.java Discountable.java Reccomendation_Engine.java
```

## How to Run

```bash
java E_CommerceReccomendEngine
```

## Example Menu

```text
=== MAIN MENU ===
1. Search & Buy Items
2. View Cart & Remove Items
3. Get a Recommendation
4. Checkout & Exit
```

## Future Improvements

- Add more product categories
- Improve recommendation scoring
- Add user accounts
- Save user carts and search history
- Add stronger input validation
- Replace text-file storage with a database
- Add a graphical user interface
- Rename classes and methods using standard Java naming conventions
- Add unit tests for cart, discount, and recommendation behavior

## Author

Created as a Java learning project focused on object-oriented design, e-commerce logic, and basic recommendation systems.
