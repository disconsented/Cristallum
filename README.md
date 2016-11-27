Goals:
- Late game resource generation without the need for destroying large portions of the world
- Infinite resources over time
- Doesn’t destroy the terrain like quarries 
- Not automated (Maybe, kinda like the idea of a harvester)
- Dangerous to gather
- Don’t add in tools or armour
- No GUI’s
- Naturally work with other mods

(Inspired by Tiberium)

Cristallum is a Quasicrystal that can be harvested in order to produce materials. The type and quantity depends on the type of Cristallum and the minerals present at the time of generation.

Cristallum comes in three varieties:
- Green (Riparius Cristallum)
 - Most common.
 - Least amount of resources produced.
 - Fastest spreading.
 - Least dangerous.
 - Biased towards common resources (percentile of what's present).
- Blue (Vinifera Cristallum)
 - Less common than green.
 - Moderate resources produced.
 - Spreads slower than green.
 - As dangerous as green but is volitile (follows the explosive characteristics of http://cnc.wikia.com/wiki/Tiberium#Vinifera).
 - No bias.
- Red (Aboreus Cristallum)
 - Least common.
 - Most resources produced.
 - Spreads the slowest.
 - Most dangerous (will melt diamond armour in 10 seconds).
 - Biased towards rare resources.

Cristallum spreads out of ‘spires’ which upon generation will scan the area around and below it to determine the amount of minerals in the area, from there it will generate the crystals based upon what it finds.  Only one type of Cristallum will be found per field.

Spires will:
- Release particles based upon its type in order to show that there is a Cristallum field.
- Be hard to destroy
- Periodically checks resources around it and will adjust the generation of new crystals accordingly however it will not allow greater than original levels in order to defeat infinite feedback loops.

When an entity comes in contact with Cristallum it will rapidly degrade armour in 30 seconds (iron armour against green Cristallum) once the armour is gone the entity will receive ichor poisoning which will slowly damage and eventually cause sudden organ failure if not cured (until I find a better idea it will be milk).

Cristallum will take over the environment in a limited area around the spire.

Refining:

1. Crush the crystals into dust
2. Insert them into centrifuge (also takes water and energy in the form of RF)
3. Take the sludge and then cook it

Centrifuge is likely going to be a multiblock that will accept water as a liquid or in a bucket and crystals. It will likely output sludge as a liquid or via a bucket.

Alternate process:
- Harvest -> Crush -> Centrifuge -> Purge -> Resources
- Crushing, Centrifuge, Purge will all be their own blocks all of which require resources
- Crusher will convert raw crystals into dust using power
- Centrifuge will take power, dust and lava (/?) to reduce the crystals down to sludge
- Purger will take the sludge, power and water and the output the final materials

Alternate process 2:
- Harvest -> Electrolysis
- One step processing with lots of flash electrical effects (takes RF) (https://en.wikipedia.org/wiki/Electrometallurgy, thanks abelistah!)

Harvesters (possibility) in order to automatically gather resources.
