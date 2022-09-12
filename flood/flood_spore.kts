@file:Depends("flood/flood_settings", "调用配置")

package flood

import arc.*
import arc.graphics.*
import arc.math.*
import arc.math.geom.*
import arc.struct.*
import arc.util.*
import mindustry.Vars.*
import mindustry.content.*
import mindustry.entities.bullet.*
import mindustry.game.*
import mindustry.gen.*
import mindustry.ui.*
import mindustry.world.*
import mindustry.world.blocks.defense.*
import mindustry.world.blocks.environment.*
import mindustry.world.meta.*
import java.util.*

import flood.flood_settings

fun targetSpore(): FloatArray {
    var ret: FloatArray? = null
    var iterations = 0
    while (ret == null && iterations < 10000 && Groups.player.size() > 0) {
        iterations++
        val player: Player = Groups.player.index(Mathf.random(0, Groups.player.size() - 1))
        if (player.unit() == null || player.x.toInt() == 0 && player.y.toInt() == 0) continue
        val unit: mindustry.gen.Unit! = player.unit()
        ret = floatArrayOf(
            unit.x + Mathf.random(-sporeTargetOffset, sporeTargetOffset),
            unit.y + Mathf.random(-sporeTargetOffset, sporeTargetOffset)
        )
        val retTile: mindustry.world.Tile! = world.tileWorld(ret[0], ret[1])

        // target creeperableTiles only
        if (creeperableTiles.contains(retTile)) {
            return ret
        }
    }
    return ret ?: floatArrayOf(0f, 0f)
}

fun sporeCollision(x: Float, y: Float) {
    val tile: mindustry.world.Tile! = world.tileWorld(x, y)
    if (invalidTile(tile)) return
    Call.effect(Fx.sapExplosion, x, y, sporeRadius, Color.blue)
    depositCreeper(tile, sporeRadius, sporeAmount)
}