# KP-RPG - cause roleplaying should be free

> What man is a man who does not make the world better.
>
> -- Balian, Kingdom of Heaven

![Dependabot](https://flat.badgen.net/dependabot/Paladins-Inn/kp-rpg/?icon=dependabot)
![Maven](https://github.com/Paladins-Inn/kp-rpg/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Docker Repository on Quay](https://quay.io/repository/klenkes74/kp-rpg-discord-bot/status "Docker Repository on Quay")](https://quay.io/repository/klenkes74/kp-rpg-discord-bot)

## Abstract
This is a "theatre-of-mind" support for online gaming. It started as pure die rolling bot but aims as support for
playing online like a VTT without a table. So no battlemaps, but handouts, die rolling and then special support for
several RPG systems. For the personal use of the maintainer, the systems TORG:Eternity and HeXXen are the main targets,
but other systems will be added eventually.


## License
The license for the software is LGPL 3.0 or newer. Parts of the software may be licensed under other licences like MIT
or Apache 2.0 - the files are marked appropriately. Patternfly4 is published unter MIT license.


## Architecture

tl;dr (ok, only the bullshit bingo words):
- Immutable Objects (where frameworks allow)
- Relying heavily on generated code
- 100 % test coverage of human generated code
- Every line of code not written is bug free!

Code test coverage for human generated code should be 100%, machinge generated code is considered bugfree until proven
wrong. Every line that needs not be written is a bug free line without need to test it. So aim for not writing code.


## Distribution
The software is distributed via quay.io. You find the images as

- quay.io/klenkes74/kp-rpg-discord-bot:1.1.0 (stable release)
- quay.io/klenkes74/kp-rpg-discord-bot:latest (bleeding edge)

The images are prepared for consumption by OpenShift 3.11, so they run without any problems on kubernetes, too.
They need a mongodb to run.

[Paladins Inn](https://www.paladins-inn.de) operates two instances of the discord bot. They can be invited by anyone as

- [Production (1.1.0)](https://discordapp.com/oauth2/authorize?scope=bot&client_id=794193453403734066&permissions=1882512464)
  Even if tagged "Production" the service is provided with out any guarantee. Unless you pay, the service is provided as
  is and may be revoked at any time without notice. Having said that, we try to keep it up as best as possible.
- [Development (1.2.0-SNAPSHOT, latest)](https://discordapp.com/oauth2/authorize?scope=bot&client_id=800069820812886036&permissions=1882512464) 
  Development means development. It may be behaving strangely or go offline any time for a prolonged time. Use for
  checking the bleeding edge features.


## Distribution
The software is distributed via quay.io. You find the images as

- quay.io/klenkes74/kp-rpg-discord-bot:1.1.0 (stable release)
- quay.io/klenkes74/kp-rpg-discord-bot:latest (bleeding edge)

The images are prepared for consumption by OpenShift 3.11, so they run without any problems on kubernetes, too.
They need a mongodb to run.

[Paladins Inn](https://www.paladins-inn.de) operates two instances of the discord bot. They can be invited by anyone as

- [Production (1.1.0)](https://discordapp.com/oauth2/authorize?scope=bot&client_id=794193453403734066&permissions=1882512464)
  Even if tagged "Production" the service is provided with out any guarantee. Unless you pay, the service is provided as
  is and may be revoked at any time without notice. Having said that, we try to keep it up as best as possible.
- [Development (1.2.0-SNAPSHOT, latest)](https://discordapp.com/oauth2/authorize?scope=bot&client_id=800069820812886036&permissions=1882512464) 
  Development means development. It may be behaving strangely or go offline any time for a prolonged time. Use for
  checking the bleeding edge features.


## Note from the author
This software is meant do be perfected not finished.

If someone is interested in getting it faster, we may team up. I'm open for that. But be warned: I want to do it 
_right_. So no short cuts to get faster. And be prepared for some basic discussions about the architecture or software 
design :-).

---
Bensheim, 2020-08-03
