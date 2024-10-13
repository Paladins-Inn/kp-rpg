# KP-RPG - cause roleplaying should be free

> What man is a man who does not make the world better.
>
> -- Balian, Kingdom of Heaven

[![Release](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/release.yml/badge.svg)](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/release.yml)
[![github-pages](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/github-pages.yml/badge.svg)](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/github-pages.yml)
[![CodeQL](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/codeql-analysis.yml)
[![CI](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/ci.yml/badge.svg)](https://github.com/Paladins-Inn/kp-rpg/actions/workflows/ci.yml)

## Abstract

The base library for RPG systems. 
It contains base information for implementing support software for playing tabletop rpgs.

## License

The license for the software is LGPL 3.0 or newer.
Parts of the software may be licensed under other licences like MIT or Apache 2.0 - the files are marked appropriately.
Patternfly4 is published unter MIT license.

## Architecture

tl;dr (ok, only the bullshit bingo words):

- Immutable Objects (where frameworks allow)
- Relying heavily on generated code
- 100 % test coverage of human generated code
- Every line of code not written is bug free!

Code test coverage for human generated code should be 100%, machine generated code is considered bugfree until proven wrong.
Every line that needs not be written is a bug free line without need to test it.
So aim for not writing code.

## Distribution

KP-RPG is published to maven central.

## Note from the author

This software is meant do be perfected not finished.

If someone is interested in getting it faster, we may team up.
I'm open for that.
But be warned: I want to do it _right_.
So no short cuts to get faster.
And be prepared for some basic discussions about the architecture or software design :-).
Just because I love them

---
Bensheim, 2020-08-03, 2024-10-13
