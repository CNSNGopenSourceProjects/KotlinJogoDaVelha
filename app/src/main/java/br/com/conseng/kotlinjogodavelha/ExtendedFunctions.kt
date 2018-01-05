package br.com.conseng.kotlinjogodavelha
/**
 * Extende funcionalidades do Kotlin.
 * 20180104 F.Camargo: implementação inicial baseada em https://www.androidauthority.com/kotlin-extension-functions-811170/
 */

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast

/**
 * Estende a função de piscar para todas as views.
 * @param [times] Número de vezes que deve piscar.  Default: [Animation.INFINITE]
 * @param [duration] Tempo em milisegundos que a view ficará "acesa".
 * @param [offset] Tempo em milisegundos que a view ficará "apagada".
 * @param [minAlpha] Visibilidade mínima da view, quando "apagada".
 * @param [maxAlpha] Visibilidade máxima da view, quando "acesa".
 * @param [repeatMode] Modo de repetição.
 */
fun View.blink(times: Int = Animation.INFINITE,
               duration: Long = 200L, offset: Long = 200L,
               minAlpha: Float = 0.0f, maxAlpha: Float = 1.0f,
               repeatMode: Int = Animation.REVERSE) {
    startAnimation(AlphaAnimation(minAlpha, maxAlpha).also {
        it.duration = duration
        it.startOffset = offset
        it.repeatMode = repeatMode
        it.repeatCount = times
    })
}

/**
 * Deixa a view visível.
 */
fun View.show() { visibility = View.VISIBLE }

/**
 * Deixa a view invisível.
 */
fun View.hide() { visibility = View.GONE }

/**
 * Estende a opção de mensagem para que fique mais simples de ser utilizada no programa. *
 * @param [text]     The text to show.  Can be formatted text.
 * @param [duration] How long to display the message.  Default: "Toast.LENGTH_LONG"
 *
 */
fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, duration).show()
}