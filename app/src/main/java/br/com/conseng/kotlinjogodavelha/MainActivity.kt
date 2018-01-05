package br.com.conseng.kotlinjogodavelha

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    /**
     * Preserva os lances do jogador que marca X.
     */
    val player_X = arrayListOf<Int>()

    /**
     * Preserva os lances do jogador que marca O.
     */
    val player_O = arrayListOf<Int>()

    /**
     * Estabelece um padrão para identificar os jogadores.
     */
    enum class Player { marca_X, marca_O }

    /**
     * Identifica o jogador que jogará.
     */
    var currentPlayer = Player.marca_X

    /**
     * Referencia todas as casas do tabuleiro
     */
    val tabuleiro = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Aponta para todas as casas do tabuleiro
        tabuleiro.add(btn1)
        tabuleiro.add(btn2)
        tabuleiro.add(btn3)
        tabuleiro.add(btn4)
        tabuleiro.add(btn5)
        tabuleiro.add(btn6)
        tabuleiro.add(btn7)
        tabuleiro.add(btn8)
        tabuleiro.add(btn9)

        btnRestart.setOnClickListener { iniciarJogo() }
    }

    /**
     * Processa uma jogada, alternando o jogador.
     * Atualiza o tabuleiro e impede que a casa utilizada uma segunda vez.
     * Verifica o status do jogo identificando o termino com vitória ou empate.
     * Em caso de vitória, deixa as casas vitoriosas piscando.
     * @param [btnSelected] Jogada.
     */
    fun play(btnSelected: Button) {
        var vitoria: Combinacao? = null
        val casa = whoAmI(btnSelected)

        when (currentPlayer) {
            Player.marca_X -> {
                btnSelected.text = "X"
                btnSelected.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPlayerX))
                player_X.add(casa)
                currentPlayer = Player.marca_O
                vitoria = checkResult(player_X)
            }
            Player.marca_O -> {
                btnSelected.text = "O"
                btnSelected.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPlayerO))
                player_O.add(casa)
                currentPlayer = Player.marca_X
                vitoria = checkResult(player_O)
            }
        }
        btnSelected.isClickable = false
        if (null != vitoria) {      // Alguém ganhou.  Termina o jogo.
            //TODO: faz os botões vitoriosos piscarem
            fimDeJogo(false, btnSelected, vitoria)
        } else if ((player_O.size + player_X.size) > 8) {   // Esgotou as jogadas?
            fimDeJogo(true, btnSelected, vitoria)
        }
    }

    /**
     * Sinaliza o fim do jogo mostrando uma mensagem e bloqueando todas as casas do tabuleiro.
     * Desativa todos os botões para impedir alterações no tabuleiro.
     * @param [empatou] Indica se o jogo terminou empatado [true] ou com um vitorioso [false]
     * @param [ganhador] Aponta para o (eventua) vencedor do jogo.
     * @param [seq] Sequencia de casas da [Combinacao] vencedora.
     */
    fun fimDeJogo(empatou: Boolean, ganhador: Button, seq:Combinacao?) {
        for (botao in tabuleiro) {      // Bloqueia todos os botões
            botao.isClickable = false
            if (!empatou) {             // Deixa as casas vencedoras piscando
                val casa = whoAmI(ganhador)
                if ((casa>0) and seq?.contain(casa)!!) {
                    ganhador.blink()    // Deixa a casa piscando
                }
            }
        }
        val mensagem = if (empatou) "O jogo terminou empatado" else "O jogador %s ganhou!".format(ganhador.text.toString())
        Toasted(mensagem)
    }

    /**
     * Prepara o tabuleiro para um novo jogo:
     *  - Limpa as jogadas anteriores.
     *  - Desativa o pisca.
     *  - Ativa os botões.
     */
    fun iniciarJogo() {
        player_X.clear()
        player_O.clear()
        for (botao in tabuleiro) {
            botao.isClickable = true
            botao.text = ""
            botao.setBackgroundColor(ContextCompat.getColor(this, R.color.button_material_light))
            botao.clearAnimation()
        }
        currentPlayer = Player.marca_X
        Toasted("Pronto para um novo jogo")
    }

    /**
     * Identifica a casa do tabuleiro e atualiza o jogo.
     * @param [view] Casa (botão) da jogada.
     * @return Casa no tabuleiro.
     */
    fun whoAmI(view: View): Int = when (view.id) {
        R.id.btn1 -> 1
        R.id.btn2 -> 2
        R.id.btn3 -> 3
        R.id.btn4 -> 4
        R.id.btn5 -> 5
        R.id.btn6 -> 6
        R.id.btn7 -> 7
        R.id.btn8 -> 8
        R.id.btn9 -> 9
        else -> 0       // Não pertence ao tabuleiro
    }

    /**
     * Preserva uma combinação válida de vitória do jogo.
     */
    data class Combinacao(val pos1: Int, val pos2: Int, val pos3: Int) {
        /**
         * Verifica se ocorreu a combinação de jogadas.
         * @param [jogadas] Todas as jogadas de um jogador.
         * @return [true] se a combinação foi encontrada.
         */
        fun contain(jogadas: ArrayList<Int>): Boolean {
            var achou: Boolean = false
            if (jogadas.size >= 3) {
                achou = jogadas.containsAll(listOf(pos1, pos2, pos3))
            }
            return achou
        }

        /**
         * Verifica se uma determinada casa do tabuleiro faz parte desta combinação.
         * @param [casa] Casa do tabuleiro a ser analisada [1~9].
         * @return [true] se a [casa] pertence à esta combinação.
         */
        fun contain(casa: Int): Boolean = casa.equals(pos1) or casa.equals(pos2) or casa.equals(pos3)
    }

    /**
     * Todas as comnicações vitoriozas do jogo.
     */
    val combinacoes = listOf<Combinacao>(
            Combinacao(1, 2, 3),
            Combinacao(4, 5, 6),
            Combinacao(7, 8, 9),
            Combinacao(1, 4, 7),
            Combinacao(2, 5, 8),
            Combinacao(3, 6, 9),
            Combinacao(1, 5, 9),
            Combinacao(3, 5, 7))

    /**
     * Verifica se alguem ganhou o jogo, avaliando todos os possíveis resultados.
     * @param [jogadas] Lista de jogadas a ser analisada.
     * @result Se não houve vitória, retorna [null]. Caso contrário, indica a sequencia vencedora [Combinacao].
     */
    fun checkResult(jogadas: ArrayList<Int>): Combinacao? {
        for (seq in combinacoes) {
            if (seq.contain(jogadas)) {
                return seq
            }
        }
        return null
    }


}
