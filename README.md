<p align="center">
  <a href="https://github.com/Aymane11/SmartDoor">
     <h3 align="center">SmartDoor</h3>
  </a>


  <p align="center">
    SmartDoor that grants access if everyone wears a face mask.
    <br />
    <a href="#about-the-project">View Demo</a>
    ·
    <a href="https://github.com/Aymane11/SmartDoor/issues">Report Bug</a>
  </p>
</p>

---

## Table of Contents

* [About the Project](#about-the-project)
  * [Built Using](#built-using)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Usage](#usage)



## About The Project
<div align="center">
    <img alt="Demo" src="../main/src/main/resources/images/demo.gif" width=60%">
</div>


With last Covid-19 pandemic, wearing a face mask became a necessity, thats why we made a SmartDoor application, that grants access to users only if they wear a face mask. It's also included with an administration interface to edit or add admins and view old sessions stored in the database.


### Built Using

* [Java](https://www.java.com/)
    * [JavaFx](https://openjfx.io/)
    * [OpenCV](https://opencv.org/)
    * [deeplearning4j](https://deeplearning4j.org/)
* [Python](https://www.python.org/)
    * [Tensorflow](https://www.tensorflow.org/)
    * [Keras](https://keras.io/)
* [Caffe](https://caffe.berkeleyvision.org/)
* [MobileNetV2](https://arxiv.org/abs/1801.04381)



## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

- Java 8 or later
- Maven
- A MySQL database

### Usage

#### 1- Clone the repo :

    $ git clone https://github.com/Aymane11/SmartDoor.git

#### 2- Install requirements :

    $ cd SmartDoor
    $ mvn install

#### 3- Set the environment :

    $ cd src/main/resources
    $ cp env.sample .env

Then change the variables values with your database credentials.

#### 4- Set up the database :

The database schema file `src/main/resources/db/db_scheme.sql` should be loaded in the database that you'll assign for the project.

#### 5- Run the program :

In the root folder run the following command:

    $ mvn compile exec:java
##### Admin Dashboard

A default admin (admin:password) is already configured in the database.