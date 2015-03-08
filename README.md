# Evolve
## Overview
Evolve is a cellular automata/discreet time simulation. It started as a way for me to learn Scala, but has become a test bed for me to learn other technologies (AngularJS, Jetty, Web Services).

The project is split into two parts Evolve (this project) and [EvolveWS](https://github.com/Graeme-Miller/EvolveWS). Evolve is a Scala application. It creates a cellular automata (basically a 2 dimensional grid that can be occupied by actors). The automata is transformed by allowing each actor to affect the boards state, this is called a tick. The current state is made available via a web service.

EvolveWS is a JavaScript/AngularJS application that queries the current state and then attempts to display it in a form that a human can understand. There is a picture mode, that attempts to give a graphically overview. There is also a species mode that associates a colour with a particular species to try and give the user an overview of the diversity of the current state.

## Current State
Evolve currently has one type of actor, and that is a plant. Plants can interact with each other by producing offspring. When plants create offspring, the offspring get qualities from their parents, affected by a random value. The main qualities are their max age, water need and chance of propagation. Plants that increase their qualities are more likely to survive and produce more plants. Hence, over time, the average value for favourable qualities goes up.

## Future Plans
Replace plants with animals. Have the animals be controlled by machine learning.

## Ghost
Their is a spooky ghost (see picture below) currently haunting the cellular automaton. He destroys plants in a radius around him. He exists purely to mix things up.

## How to use
Ensure you have Java and Maven installed. Checkout Evolve and [EvolveWS](https://github.com/Graeme-Miller/EvolveWS). Run both projects with “mvn jetty:run”. View the front end on “http://localhost:8080/”

## Screen Shots
### In pic mode
![alt text](https://github.com/Graeme-Miller/Evolve/blob/master/img/inprogress.png "In pic mode")

### In species mode; species is broken down in multiple subspecies
![alt text](https://github.com/Graeme-Miller/Evolve/blob/master/img/merged.png "In species mode; species is broken down in multiple subspecies")
