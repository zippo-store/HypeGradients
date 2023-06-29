[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![AGPLv3 License][license-shield]][license-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h1>HypeGradients</h1>
  <p>
    Modify every message in minecraft with ease and make your server beautiful
    <br />
    <br />
    <a href="https://github.com/zippo-store/HypeGradients/issues">Report Bug</a>
    ·
    <a href="https://github.com/zippo-store/HypeGradients/issues">Request Feature</a>
  </p>
</div>




<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#license">License</a></li>
    <li>
        <a href="#API">API</a>
        <ul>
            <li><a href="#Maven">Maven</a></li>
            <li><a href="#Gradle">Gradle</a></li>
            <li><a href="#Usage">Usage</a></li>
        </ul>
    </li>

  </ol>
</details>




<!-- ABOUT THE PROJECT -->

## About The Project

HypeGradients is a spigot plugin that takes every message from the server and modifies it how you want.

Initially, HypeGradients was made for making gradients with the help
of [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI).
But I found out that I can do more and so with the help of [ProtocolLib](https://github.com/dmulloy2/ProtocolLib) I had
the possibility
to get every message of what the server sends to the client.

Check out the [roadmap](#roadmap) for what features will be coming next!

<!-- ROADMAP -->

## Roadmap

- [ ] Make a better system for animations
- [ ] Create a documentation for the api
- [ ] Check if 1.20 packets does not have any problems
- [ ] Make a reload system for every custom packet

Contributions are what make the open-source community such an amazing place to learn, inspire, and create.  
Any contributions you make are **greatly appreciated**!  
If you're new to contributing to open-source projects,
you can follow [this](https://docs.github.com/en/get-started/quickstart/contributing-to-projects) guide to get
up-to-speed.




<!-- LICENSE -->

## License

Distributed under the MIT License
See [LICENSE][license-url] for more information.

## API

If you want to use HypeGradients in your project you can use [Maven](#maven) or [Gradle](#gradle), thanks
to [JitPack](https://jitpack.io)!
I recommend to use the Build module because it contains everything

[![](https://jitpack.io/v/zippo-store/HypeGradients.svg "Check out JitPack for more information")](https://jitpack.io/#zippo-store/HypeGradients)

### Maven

#### Step 1. Add the JitPack repository to your build file

```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

#### Step 2. Add the dependency

```xml
    <dependency>
        <groupId>com.github.zippo-store.HypeGradients</groupId>
        <artifactId>HypeGradients-Build</artifactId>
        <version>1.0.7</version>
        <scope>provided</scope>
    </dependency>
```

### Gradle

#### Step 1. Add it in your root build.gradle at the end of repositories:

```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```

#### Step 2. Add the dependency

```groovy
    dependencies {
        implementation 'com.github.zippo-store.HypeGradients:HypeGradients-Build:1.0.7'
    }
```

### Usage

Check out this example project that uses HypeGradients API
![Static Badge](https://img.shields.io/badge/HypeGradientsGUI-3?style=plastic&logo=github&logoColor=black&label=Check%20out&link=https%3A%2F%2Fgithub.com%2FDoubleNico%2FHypeGradients-GUI)

If you wanna know more about the API, take a look at the wiki! (TODO)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/zippo-store/HypeGradients.svg?style=for-the-badge

[contributors-url]: https://github.com/zippo-store/HypeGradients/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/zippo-store/HypeGradients.svg?style=for-the-badge

[forks-url]: https://github.com/zippo-store/HypeGradients/network/members

[stars-shield]: https://img.shields.io/github/stars/zippo-store/HypeGradients.svg?style=for-the-badge

[stars-url]: https://github.com/zippo-store/HypeGradients/stargazers

[issues-shield]: https://img.shields.io/github/issues/zippo-store/HypeGradients.svg?style=for-the-badge

[issues-url]: https://github.com/zippo-store/HypeGradients/issues

[license-shield]: https://img.shields.io/github/license/zippo-store/HypeGradients.svg?style=for-the-badge

[license-url]: https://github.com/zippos-tore/HypeGradients/blob/master/LICENSE