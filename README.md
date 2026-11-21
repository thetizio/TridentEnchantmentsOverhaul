This mod enhances tridents by modifying how enchantments work:

1) impaling: instead of dealing increased damage to water mobs, it deals 1 extra damage each level against every mob. In addition, any level of impaling allows the trident to pierce through mobs, dealing damage to every pierced mob as long as it isn’t too close to the one that was hit before

2) loyalty: instead of making the trident return to you after throwing it, it makes the trident stay in your inventory even after throwing it. Just like infinity on bows, you won’t be able to pick up tridents thrown by tridents with loyalty, you can also choose to make it increase trident throw strength and trident throw accuracy

3) riptide: after launching the player, it will give 5 seconds of slow falling to prevent fall damage, can be used outside water

4) channeling: lightning on the target will always spawn, regardless of any condition

If you are <ins>playing in 1.20.1</ins>, you can change features individually through config:
- if impaling should increase damage against every mob ---- default true
- how much impaling should increase damage ---- default 1.0
- if impaling should make the trident pierce through mobs --- default true
- if loyalty should make trident stay in your inventory --- default true
- how much loyalty increases trident throw strength --- default 0.0
- how much loyalty increases trident throw accuracy --- default 0.0
- if riptide should give you slow falling after use --- default true
- how long should slow fall from riptide last in seconds --- default 5
- if riptide can be used outside water --- default true
- if channeling should always summon lightning when hitting mobs --- default true

If you are <ins>playing in 1.21.1 or above</ins>, you can change most features through config like in 1.20.1, but the following are data-driven instead:

- if impaling should increase damage against every mob ---- default true
- how much impaling should increase damage ---- default 1.0
- if channeling should always summon lightning when hitting mobs --- default true

these can be modified by overwriting data\minecraft\enchantment\impaling.json (or channeling.json)
