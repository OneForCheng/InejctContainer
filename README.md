## InjectContainer



### 1. 概述

InjectContainer 是一个轻量级（简单实现）的依赖注入框架。简而言之，你可以通过引入 InjectContainer 提供的注解来声明需要自动注入的对象依赖，其创建和销毁过程完全将由 InjectContainer 框架自动完成。

<br />

### 2. 一个简单样例

通过一个简单的样例来了解一下如何使用 `InjectContainer`。

<br />

如下，`Foo` 是一个具有默认构造函数（无参构造函数）的对象：

```java
public class Foo {
  public Foo() {}
}

InjectContainer container = new InjectContainer();
Foo foo = (Foo)container.getInstance(Foo.class); // 可以正确获取 Foo 的实例
```

首先，实例化一个 `InjectContainer` 对象 `container`，然后通过把对象 `Foo` 的类型传递给 `container` 的 `getInstance` 方法，就可以成功获得一个对象 `Foo` 的实例 `foo`。

<br />


但如果对象没有一个共有的构造函数，如下对象 `Bar` ：

```java
public class Bar {
  private Bar() {}
}

InjectContainer container = new InjectContainer();
Bar bar = (Bar)container.getInstance(Bar.class); // Throw Exception: no accessible constructor for injection class Bar
```

那么当用 `container` 的 `getInstance` 方法去获取对象 `Bar` 的实例时，将会失败并抛出异常： no accessible constructor for injection class Bar 。

<br />

另外，如果对象带有一个有参构造函数，如下对象 `Baz` ：

```java
public class Baz {
  public Baz(Qux qux) {}
}

public class Qux {}

InjectContainer container = new InjectContainer();
Baz baz = (Baz)container.getInstance(Baz.class); // Throw Exception: no accessible constructor for injection class Baz
```

同样用 `container` 的 `getInstance` 方法去获取对象 `Baz` 的实例时，将会失败并抛出异常：no accessible constructor for injection class Baz。因为对象 Baz 的构造函数并没有用 `@Inject` 注解来声明，那么 `container` 就不会将构造函数 `Baz` 中的参数 `qux` 识别为需要自动注入的对象，所以无法创建对象 `Baz` 的实例。

<br />

### 3. 使用 @Inject 注解

通过使用 `@Inject` 注解来声明对象的构造函数，使其参数可以自动注入。

<br />

如下，对象 `Fred` 带有一个有参构造函数，并且其用 `@Inject` 注解标识：

```java
public class Fred {
  @Inject
  public Fred(Qux qux) {}
}

public class Qux {}

InjectContainer container = new InjectContainer();
Fred fred = (Fred)container.getInstance(Fred.class); // 可以正确获取 Fred 的实例
```

当 `container` 使用 `getInstance` 方法去获取对象 `Fred` 的实例时，通过扫描对象 `Fred` 的构造函数，可以成功地获取到带有 `@Inject` 注解的带参构造函数 `Fred`，进而能够进一步自动构建对象 `Qux` 的实例作为构造函数 `Fred` 的参数，最终成功创建对象 `Fred` 的实例。

<br />

不过，如果考虑对象有多个共有的构造函数，如下对象 `Thud` ：

```java
public class Thud {
  public Thud() {}

  @Inject
  public Thud(Qux qux) {}
}

// 或者

public class Thud {
  @Inject
  public Thud(int i) {}

  @Inject
  public Thud(Qux qux) {}
}

public class Qux {}

InjectContainer container = new InjectContainer();
Thud thud = (Thud)container.getInstance(Thud.class); // Throw Exception: multiple injectable constructor for injection class Thud
```

当 `container` 使用 `getInstance` 方法去获取对象 `Thud` 的实例时，由于扫描到对象 `Thud` 有多个可以自动注入的构造函数，不知道应该用哪一个构造函数来实例化对象 `Thud`，因此将会失败并抛出异常： multiple injectable constructor for injection class Thud 。

<br />

另外，如果依赖注入的对象存在循环依赖：

```java
public class Red {
  @Inject
  public Red(Blue blue) {}
}

public class Blue {
  @Inject
  public Blue(Red Red) {}
}

InjectContainer container = new InjectContainer();
Red red = (Red)container.getInstance(Red.class); // Throw Exception: circular dependency on constructor, the class is Blue
Blue b = (Blue)container.getInstance(Blue.class); // Throw Exception: circular dependency on constructor, the class is Red
```

当 `container` 使用 `getInstance` 方法去获取对象 `Red` 的实例时，通过扫描对象 `Red` 的构造函数，可以成功地获取到带有 `@Inject` 注解的带参构造函数 `Red`，进而尝试自动创建其依赖的参数 `blue`，但创建对象 `Blue` 的实例的时候会发现其依赖对象 Red 的实例，因而形成了一个循环的依赖，导致无法自动的创建相应的依赖，最终将会执行失败并抛出异常： circular dependency on constructor, the class is Blue 。 获取 `Blue` 的实例同理。

<br />

最后，如果我们将 `@Inject` 注解不只用在对象的构造函数上，也尝试应用于字段或方法上：

```java
public class Fum {
  @Inject
  public Fum(Foo foo) {}

  @Inject  // '@Inject' not applicable to field
  public Qux qux;

  @Inject  // '@Inject' not applicable to method
  private void install(Foo foo)
}

public class Foo {}
public class Qux {}
```

可以看到，编辑器或运行程序时将会提醒： `@Inject` 对于字段或方法不适用
