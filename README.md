# aos
> A jahia implementatinon of the AOS (Animate on scroll) library https://github.com/michalsnik/aos

## Installation

Please read the dedicated tutorial on https://academy.jahia.com/training-kb/tutorials/administrators/installing-a-module and select the Animate on scroll module from the store.

## Usage

The idea of this module add an animate on scroll on any `jmix:droppableContent`
Simply enable the mixin *Animate on scroll* on your content and choose your animation. 

### Here are the properties

| Label | Name | Description | Default value |
| --- | --- | --- | --- |
| **Animation** | `aosAnimation` | There are serveral predefined animations you can use already. See the list below. | `fade` | 
| **Change offset to trigger animations sooner or later (px)** | `aosOffset` | Set an offset to trigger animations sooner or later, to instance `200` to trigger the animation after 200px |  `120` | 
| **Duration of animation (ms)** | `aosDuration` | Set the duration of animation, for instance `600` st set an animation of 600 ms | `400` | 
| **Choose timing function to ease elements** | `aosEasing` | You can choose one of these timing function to animate elements nicely. See the list below. | `ease` | 
| **Delay animation (ms)** | `aosDelay` | Set the aelay animation, for instance `300` to start the animation after 300 ms | `0` | 
| **Anchor element, whose offset will be counted to trigger animation instead of actual elements offset** | `aosAnchor` | You can set an ID such as `#mylink` | | 
| **Anchor placement** | `aosAnchorPlacement`  | Which one position of element on the screen should trigger animation. See the list below. | `bottom-top`
| **Animation should happen only once** | `aosOnce` | Choose wheter animation should fire once, or every time you scroll up/down to element | `false` |

### Animations

There are serveral predefined animations you can use already:

- **Fade animations:**
    - fade
    - fade-up
    - fade-down
    - fade-left
    - fade-right
    - fade-up-right
    - fade-up-left
    - fade-down-right
    - fade-down-left
- **lip animations:**
    - flip-up
    - flip-down
    - flip-left
    - flip-right
- **Slide animations:**
    - slide-up
    - slide-down
    - slide-left
    - slide-right
- **Zoom animations:**
    - zoom-in
    - zoom-in-up
    - zoom-in-down
    - zoom-in-left
    - zoom-in-right
    - zoom-out
    - zoom-out-up
    - zoom-out-down
    - zoom-out-left
    - zoom-out-right

Please get an example of all animations at http://michalsnik.github.io/aos/

### Anchor placement:

- top-bottom
- top-center
- top-top
- center-bottom
- center-center
- center-top
- bottom-bottom
- bottom-center
- bottom-top


### Easing functions:

You can choose one of these timing function to animate elements nicely:

- linear
- ease
- ease-in
- ease-out
- ease-in-out
- ease-in-back
- ease-out-back
- ease-in-out-back
- ease-in-sine
- ease-out-sine
- ease-in-out-sine
- ease-in-quad
- ease-out-quad
- ease-in-out-quad
- ease-in-cubic
- ease-out-cubic
- ease-in-out-cubic
- ease-in-quart
- ease-out-quart
- ease-in-out-quart
