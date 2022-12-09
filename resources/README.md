# Module resources

The only module which is responsible for storing android resources (strings, drawable and other reusable resources from `res`).
The module provides resources for each required module when needed.

This approach helps to solve the problem of resource duplication across modules.

We don't keep colors in xml, except particular cases when we need no define a color in xml theme or when using android views. We store colors in composables.