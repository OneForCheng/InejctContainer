## InjectContainer



### 1. 概述

InjectContainer 是一个轻量级（简单实现）的依赖注入框架。简而言之，你可以通过引入 InjectContainer 提供的注解来声明需要自动注入的对象依赖，其创建和销毁过程完全将由 InjectContainer 框架自动完成。

<br />

### 2. 一个简单样例

通过一个简单的样例来了解一下如何使用 `InjectContainer`。

<br />

如下，`A` 是一个具有默认构造函数（无参构造函数）的对象：

```java
public class A {
  public A() {}
}

InjectContainer container = new InjectContainer();
A a = (A)container.getInstance(A.class); // 可以正确获取 A 的实例
```

首先，实例化一个 `InjectContainer` 对象 `container`，然后通过把对象 `A`  的类型传递给 `container` 的 `getInstance` 方法，就可以获得一个对象 `A` 的实例。

<br />

但如果对象 `A` 没有一个共有的构造函数：

```Java
public class A {
  private A() {}
}

InjectContainer container = new InjectContainer();
A a = (A)container.getInstance(A.class); // Throw Exception: no accessible constructor for injection class A
```

那么当用 `container` 的 `getInstance` 方法去获取对象 A 的实例时，将会报错抛出一个异常。

<br />

另外，如果对象 A 带有一个有参构造函数：

```java
public class A {
  public A(B b) {}
}

public class B {}

InjectContainer container = new InjectContainer();
A a = (A)container.getInstance(A.class); // Throw Exception: no accessible constructor for injection class A
```

同样用  `container` 的 `getInstance` 方法去获取对象 `A` 的实例时，也会抛出一个异常。因为对象 A 的构造函数并没有用 `@Inject` 注解来声明，那么 `container` 就不会将构造函数 `A` 中的参数 `b` 识别为需要自动注入的对象，所以无法创建对象 `A` 的实例。

<br />

### 3. 使用 @Inject 注解

通过使用 `@Inject` 注解来声明对象的构造函数，使其参数可以自动注入。

<br />

如下，对象 A 带有一个有参构造函数，并且其用 `@Inject` 注解标识：

```java
public class A {
  @Inject
  public A(B b) {}
}

public class B {}

InjectContainer container = new InjectContainer();
A a = (A)container.getInstance(A.class); // 可以正确获取 A 的实例
```

当 `container` 使用 `getInstance` 方法去获取对象 `A` 的实例时，通过扫描对象 `A` 的构造函数，可以成功地获取到带有 `@Inject` 注解的带参构造函数 `A`，进而能够进一步自动构建对象 `B` 的实例作为构造函数 `A` 的参数，最终将对象 `A` 的实例创建成功。

<br />

不过，如果考虑对象 `A` 有多个共有的构造函数：

```java
public class A {
  public A() {}

  @Inject
  public A(B b) {}
}

// 或者

public class A {
  @Inject
  public A(int i) {}

  @Inject
  public A(B b) {}
}

public class B {}

InjectContainer container = new InjectContainer();
A a = (A)container.getInstance(A.class); // Throw Exception: duplicated constructor for injection class A
```

当 `container` 使用 `getInstance` 方法去获取对象 `A` 的实例时，由于扫描到对象 `A` 有多个可以自动注入的构造函数，不知道应该用哪一个构造函数来实例化对象 `A`，因此将会报错抛出一个异常。

<br />

另外，如果依赖注入的对象存在循环依赖：

```java
public class A {
  @Inject
  public A(B b) {}
}

public class B {
  @Inject
  public B(A a) {}
}

InjectContainer container = new InjectContainer();
A a = (A)container.getInstance(A.class); // Throw Exception: circular dependency on constructor , the root class is A
B b = (B)container.getInstance(B.class); // Throw Exception: circular dependency on constructor , the root class is B
```

当 `container` 使用 `getInstance` 方法去获取对象 `A` 的实例时，通过扫描对象 `A` 的构造函数，可以成功地获取到带有 `@Inject` 注解的带参构造函数 `A`，进而尝试自动创建其依赖的参数 `b`，但创建对象 `B` 的实例的时候会发现其依赖对象 A 的实例，因而形成了一个循环的依赖，导致无法自动的创建相应的依赖，最报错抛出相应的异常。

<br />

最后，如果我们将  `@Inject` 注解不只用在对象的构造函数上，也用于字段上：

```java
public class A {
    @Inject
    public A(B b) {}

    @Inject
    public C c;
}

public class B {}
public class C {}

    InjectContainer container = new InjectContainer();
    A a = (A)container.getInstance(A.class); // 可以正确获取 A 的实例
    expect(a.c).toBe(null); // 断言通过
```

`container` 使用 `getInstance` 方法可以成功获取对象 `A` 的实例，但其字段 `c` 的值仍旧为 `null`。可以看出，`InjectContainer` 中的 `@Inject` 注解只对对象的构造函数有效，就算将  `@Inject` 注解声明与字段或其他地方，也不会自动的注入相应的依赖实例。


