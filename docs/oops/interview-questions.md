# Object-Oriented Programming Interview Questions

Questions and concise answer points for computer-science graduates. Examples use Java, but the ideas apply to any object-oriented language. In an interview, state the concept, then back it with a small example or a trade-off.

## Fundamentals

1. **What is object-oriented programming?**
   - It is a way of organizing code around objects that bundle data (state) with the operations on that data (behavior), rather than around standalone functions acting on shared data.
   - For example, instead of a global `balance` variable and loose `deposit`/`withdraw` functions, an `Account` object owns its `balance` and exposes `deposit` and `withdraw` methods that keep it valid. State and the rules that protect it stay together.

2. **What is the difference between a class and an object?**
   - A class is a blueprint that defines fields and methods. An object is a concrete instance of that class, with its own values in memory.
   - `class Car { String color; }` describes what every car has; `new Car()` creates one specific car. One class can produce many independent objects.

3. **What are the four pillars of OOP?**
   - **Encapsulation** hides internal state behind a controlled interface. **Abstraction** exposes only the essential behavior and hides the details. **Inheritance** lets a type reuse and extend another. **Polymorphism** lets one interface work with many concrete types.
   - These are complementary: encapsulation and abstraction control *what is visible*, while inheritance and polymorphism control *how types relate and vary*.

4. **What is a constructor? What is a default constructor?**
   - A constructor initializes a new object and runs when it is created. If a class declares no constructor, the compiler supplies a no-argument default constructor.
   - Once you declare any constructor (for example one that takes arguments), the compiler no longer generates the default one. A common bug is expecting `new Account()` to still work after adding `Account(String owner)`.

## Encapsulation and Abstraction

5. **What is encapsulation?**
   - It keeps fields private and exposes access only through methods, so an object controls how its state is read and changed.
   - Making `balance` private and allowing changes only through `deposit` and `withdraw` lets the class reject a negative amount. If `balance` were public, any code could set it to an invalid value and no rule could stop it.

6. **How is abstraction different from encapsulation?**
   - Abstraction is about *what* an object does as seen from outside (its interface); encapsulation is about *how* that is protected and implemented internally.
   - A `List` tells you it can `add` and `get` (abstraction). Whether it stores elements in an array or linked nodes, and how it guards that storage, is encapsulation. One hides complexity; the other hides data.

7. **What do the Java access modifiers mean?**
   - `private` is visible only within the class, package-private (no modifier) within the same package, `protected` within the package and to subclasses, and `public` everywhere.
   - Default to the most restrictive level that still works. Widening access later is easy; narrowing it after callers depend on it is a breaking change.

## Inheritance and Polymorphism

8. **What is inheritance? Does Java support multiple inheritance?**
   - Inheritance lets a subclass reuse and extend a superclass with an "is-a" relationship. Java supports single class inheritance (one direct superclass) but allows a class to implement multiple interfaces.
   - A `Dog` is an `Animal`, so `Dog extends Animal` reuses `Animal`'s behavior. Java forbids extending two classes to avoid the ambiguity of inheriting conflicting implementations, but multiple interfaces are allowed because they mostly declare behavior.

9. **What is the difference between overloading and overriding?**
   - Overloading defines multiple methods with the same name but different parameter lists in the same class; it is resolved at compile time. Overriding replaces a superclass method with a new implementation in a subclass; it is resolved at run time.
   - `add(int, int)` and `add(double, double)` are overloads. A `Circle` redefining `Shape`'s `area()` is an override. Overloading is compile-time polymorphism; overriding is run-time polymorphism.

10. **How does runtime polymorphism work?**
    - When a method is called through a superclass reference, the JVM dispatches to the actual object's overriding method, not the reference type's version. This is dynamic dispatch.

    ```java
    Shape shape = new Circle(5);
    shape.area(); // calls Circle.area(), not Shape.area()
    ```

    - This lets code work against `Shape` while behaving correctly for every concrete shape. Adding a new shape needs no change to code that only uses the `Shape` interface.

11. **What do `this` and `super` do?**
    - `this` refers to the current object; `super` refers to the superclass part of it. Both can call the matching constructor (`this(...)`, `super(...)`) or access members.
    - Use `super.method()` to extend rather than fully replace inherited behavior, for example calling `super.toString()` and appending extra fields.

12. **Can static, private, or final methods be overridden?**
    - No. A `final` method cannot be overridden. A `private` method is not visible to subclasses, so a same-named method is unrelated. A `static` method belongs to the class, so redeclaring it in a subclass is *hiding*, not overriding, and is resolved by reference type.
    - This distinction matters: calling a hidden static method through a superclass reference runs the superclass version, unlike a true override.

## Abstract Classes and Interfaces

13. **What is the difference between an abstract class and an interface?**
    - An abstract class can hold state (fields), constructors, and both concrete and abstract methods; a class extends only one. An interface declares behavior, supports multiple inheritance of type, and (since Java 8) can provide `default` and `static` methods but no instance state.
    - Use an abstract class when related types share common state or implementation; use an interface to declare a capability that unrelated types can provide.

14. **When would you choose an interface over an abstract class?**
    - Choose an interface when several unrelated classes need to offer the same capability, such as `Comparable` or `Runnable`, or when a type must play multiple roles at once.
    - Choose an abstract class when the subtypes are genuinely a family that shares fields and default logic. Because Java allows only one superclass but many interfaces, interfaces are the more flexible default.

15. **What is the diamond problem and how does Java handle it?**
    - It arises when a type inherits the same method from two paths, leaving the compiler unsure which to use. Java avoids it for classes by allowing only single class inheritance.
    - With interface `default` methods, two interfaces can supply the same default. Java then forces the implementing class to override the method and can resolve explicitly with `InterfaceName.super.method()`.

## The Java Object Model

16. **What is the difference between `==` and `equals()`?**
    - `==` compares references (whether two variables point to the same object) for objects, and raw values for primitives. `equals()` compares logical equality as defined by the class.
    - Two different `String` objects with the same characters are `equals()` but not necessarily `==`. Forgetting this and comparing strings with `==` is a classic bug.

17. **What is the `equals()` and `hashCode()` contract?**
    - If two objects are `equals()`, they must return the same `hashCode()`. Equal hash codes do not require equality. Override both together or neither.
    - Break it and hash-based collections misbehave: a `HashMap` key that overrides `equals()` but not `hashCode()` may be stored in one bucket and searched in another, so `get` returns `null` for a key that is logically present.

18. **What are some methods every object inherits from `Object`?**
    - `equals()`, `hashCode()`, `toString()`, `getClass()`, and `clone()`, among others. `toString()` gives a readable form, and the others support comparison, hashing, and reflection.
    - Overriding `toString()` alone makes logging and debugging far easier, and is a low-risk habit even when the object is not used as a collection key.

19. **What is an immutable object? How do you make one in Java?**
    - An immutable object cannot change after construction. Make the class `final`, keep all fields `private final`, set them only in the constructor, expose no setters, and defensively copy any mutable fields passed in or returned.
    - `String` is immutable, which makes it safe to share across threads and to use as a `HashMap` key without worrying that its hash changes. Immutability trades some allocation cost for simpler reasoning.

## Class Relationships and Design

20. **What is the difference between association, aggregation, and composition?**
    - Association is any "uses-a" link between objects. Aggregation is a "has-a" whole/part where parts can outlive the whole. Composition is a stronger "has-a" where the part's lifetime is owned by the whole.
    - A `Course` and a `Student` are associated. A `Team` aggregates `Player`s who exist independently. A `House` composes `Room`s that are meaningless once the house is gone; destroying the house destroys its rooms.

21. **Why is composition often preferred over inheritance?**
    - Inheritance is tight coupling: a subclass depends on its superclass's implementation and inherits everything, wanted or not. Composition builds behavior by holding other objects and delegating to them, which is more flexible.
    - "Favor composition over inheritance" avoids fragile deep hierarchies. For example, a `Car` that *has an* `Engine` can swap engine types at run time, whereas `Car extends Engine` would be both wrong ("is-a") and rigid.

22. **What are the SOLID principles?**
    - **S**ingle Responsibility: a class should have one reason to change. **O**pen/Closed: open for extension, closed for modification. **L**iskov Substitution: subtypes must be usable wherever the base type is expected. **I**nterface Segregation: prefer small, focused interfaces. **D**ependency Inversion: depend on abstractions, not concrete implementations.
    - These are guidelines, not laws. They aim to keep code easy to change; applied dogmatically they can add needless abstraction, so weigh them against simplicity.

23. **What is the difference between coupling and cohesion?**
    - Coupling measures how much one module depends on another; cohesion measures how focused a single module is. Good design aims for low coupling and high cohesion.
    - A class that mixes database access, business rules, and formatting has low cohesion and usually high coupling, so a change in one concern risks breaking the others.

## Common Discussion Prompts

- Model a parking lot: which classes exist, and are `Vehicle`/`Spot` related by association, aggregation, or composition?
- Design an immutable `Money` type and explain why immutability helps.
- Given a `Shape` base type, add `Circle` and `Rectangle` and show how polymorphism avoids `if/else` on a type field.
- Explain a situation where inheritance was the wrong choice and composition fixed it.
- Why should a subclass not violate the expectations of its superclass (Liskov substitution)? Give an example that breaks it.
