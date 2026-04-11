# CurvedText - KMP Curved TextView 🌈

![Maven Central Version](https://img.shields.io/maven-central/v/com.dontsaybojio/curvedtext?color=%234c1)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.3-4285F4?logo=jetpackcompose&logoColor=white)
![License](https://img.shields.io/badge/License-GPL%20v3-blue.svg)

A **Kotlin Multiplatform** Compose library that lets you render text curved along a circular arc —
like a rainbow ☀️ or a frown 🙃. Works across Android, iOS, Desktop, and WebAssembly!

---

## ✨ Features

- 🌈 **Upward curves** — rainbow / smile style
- 🙃 **Downward curves** — frown / arc style
- 🎨 **Fully styleable** — color, font size, weight, family, letter spacing, decorations
- 📐 **Auto-sizing canvas** — the composable sizes itself to fit the text perfectly
- 🧩 **Drop-in Composable** — works just like `Text()`

---

## 📱 Platform Support

| Platform      | Supported |
|---------------|:---------:|
| Android       |     ✅     |
| iOS           |     ✅     |
| Desktop (JVM) |     ✅     |
| WebAssembly   |     ✅     |

---

## 🚀 Installation

Add the dependency to your `commonMain` source set:

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation("com.dontsaybojio:curvedtext:x.x.x")
}
```

---

## 🛠️ Usage

### Basic upward curve (rainbow 🌈)

```kotlin
CurvedText(
    text = "Hello Rainbow Text!",
    radius = 200.dp
)
```

### Downward curve (frown 🙃)

```kotlin
CurvedText(
    text = "Curved Downward",
    radius = (-150).dp,
    color = Color.Blue
)
```

### With custom style

```kotlin
CurvedText(
    text = "CURVED HEADLINE",
    radius = 150.dp,
    style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6200EE)
    )
)
```

### Mixing individual parameters

```kotlin
CurvedText(
    text = "Stylish Curve ✨",
    radius = 120.dp,
    fontSize = 20.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color(0xFFFF6B35),
    letterSpacing = 2.sp
)
```

> 💡 **Tip:** A larger `radius` creates a gentler, more subtle curve. A smaller `radius` creates a
> tighter, more dramatic arc!

---

## 📖 API Reference

### `CurvedText`

```kotlin
@Composable
fun CurvedText(
    text: String,
    radius: Dp,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
)
```

| Parameter        | Type              | Description                                                                                                                |
|------------------|-------------------|----------------------------------------------------------------------------------------------------------------------------|
| `text`           | `String`          | The text to display along the curve                                                                                        |
| `radius`         | `Dp`              | Curve radius. **Positive** = upward (smile 😊), **Negative** = downward (frown 🙁). Larger absolute values = gentler curve |
| `modifier`       | `Modifier`        | Standard Compose modifier                                                                                                  |
| `style`          | `TextStyle`       | Base text style — merged with individual parameters                                                                        |
| `color`          | `Color`           | Text color. Falls back to the color in `style` if unspecified                                                              |
| `fontSize`       | `TextUnit`        | Font size                                                                                                                  |
| `fontStyle`      | `FontStyle?`      | Italic, normal, etc.                                                                                                       |
| `fontWeight`     | `FontWeight?`     | Bold, medium, thin, etc.                                                                                                   |
| `fontFamily`     | `FontFamily?`     | Custom font family                                                                                                         |
| `letterSpacing`  | `TextUnit`        | Space between characters                                                                                                   |
| `textDecoration` | `TextDecoration?` | Underline, line-through, etc.                                                                                              |

---

## 🙌 Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request
on [GitHub](https://github.com/delacrixmorgan/curvedtext-kmp). Whether it's a bug fix, a new feature
idea, or just improving the docs — all help is appreciated! 💪
