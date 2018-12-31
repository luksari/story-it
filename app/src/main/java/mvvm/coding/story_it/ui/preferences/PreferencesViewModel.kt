package mvvm.coding.story_it.ui.preferences

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import mvvm.coding.story_it.R
import mvvm.coding.story_it.base.Converters
import mvvm.coding.story_it.base.Coroutines
import mvvm.coding.story_it.data.db.entity.Game
import mvvm.coding.story_it.data.db.entity.Player
import mvvm.coding.story_it.data.db.entity.Score
import mvvm.coding.story_it.data.db.repository.GameRepository
import mvvm.coding.story_it.data.model.GameModel
import mvvm.coding.story_it.data.model.Preferences
import mvvm.coding.story_it.data.model.Round

class PreferencesViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private var adapter : PlayerListAdapter? = null

    val playerName : MutableLiveData<String> = MutableLiveData()

    val numberOfRounds = MutableLiveData<Int>()
    val numberOfWords = MutableLiveData<Int>()
    val numberOfCharacters = MutableLiveData<Int>()
    val isRandomOrder : MutableLiveData<Boolean> = MutableLiveData()

    private val _gameModel : MutableLiveData<GameModel> = MutableLiveData()
    val gameModel : LiveData<GameModel>
        get() = _gameModel

    private var playerList : MutableList<Player> = mutableListOf()
    private val _players : MutableLiveData<List<Player>> = MutableLiveData()
    val players : LiveData<List<Player>>
        get() = _players
    private val _chosenPlayers : MutableLiveData<List<Player>> = MutableLiveData()

    private val rounds : List<Round> = listOf()
    var player : Player? = null

    private val _preferences = MutableLiveData<Preferences>()
    val preferences : LiveData<Preferences>
        get() = _preferences

    val isPossibleToStartGame = MutableLiveData<Boolean>()
    private val uiPlayerListError = MutableLiveData<String>()

    init {
        loadPlayers()

        isPossibleToStartGame.value = true
        numberOfWords.value = 2
        numberOfCharacters.value = 140
        numberOfRounds.value = 4

        adapter = PlayerListAdapter(R.layout.player_list_item, this)
    }
    //region Adapter setter getter
    fun setAdapter(players: List<Player>){
        this.adapter?.setPlayers(players)
        this.adapter?.notifyDataSetChanged()
    }
    fun getAdapter() = adapter
    //endregion

    fun refresh(){
        validateFragment()
    }
    // Adds player to DB on IO thread, then adds it to playerList that is value of MutableList to watch for updates
    fun addPlayer(){
        val name =playerName.value ?: "Player"
        if(!isPlayerInList(name)){
            player = Player(name).also {
                it.isChosen.value=true
            }
            Coroutines.ioThenMain( {gameRepository.addPlayer(player!!)
            }) {
                playerList.add(player!!)
                _players.value = playerList
            }
        }
        else{
            playerName.value = "${playerName.value}${_players.value?.size}"
        }

    }

    fun validateFragment(){

        isPossibleToStartGame.value =
                (_players.value?.filter { player -> player.isChosen.value!! }?.size in 2..35)
                && (numberOfRounds.value in 2..35)
                && (numberOfCharacters.value in 10..140)
                && (numberOfWords.value in 2..4)
    }

    private fun isPlayerInList(playerName: String) : Boolean{
        val player = _players.value?.firstOrNull { player -> player.name == playerName  }
        if(player!= null && _players.value!!.contains(player)) {
            uiPlayerListError.value = "There is already player named $playerName"
            return true
        }
        return false

    }

    fun getPlayerOfId(id: Int) = _players.value?.get(id)

    fun clearPreferences(){
        //_players.value?.forEach { player-> player.isChosen.value = false }
        numberOfRounds.value = 4
        numberOfCharacters.value = 140
        numberOfWords.value = 2
    }

    fun addGame(){
        // Filter playersList to take only players that are already chosen by CheckBox
        _chosenPlayers.value = _players.value?.filter { player -> player.isChosen.value!! }
        var preferences : Preferences? = null
        // Create scoped instance of preferences to inject it into GameModel constructor
        if(_chosenPlayers.value?.size?.compareTo(2) == 0 || _chosenPlayers.value?.size?.compareTo(2) == 1){
            preferences = Preferences(numberOfCharacters.value ?: 140,numberOfWords.value ?: 2, numberOfRounds.value ?: 4,isRandomOrder.value ?: false,  _chosenPlayers.value!!)
        }

        // GameModel stands for game state stored in app memory, it should be upserted on every Fragment/Activity onDestroy,
        // and loaded on the every Activity/Fragment onCreate
        if(preferences != null){
            _gameModel.value = GameModel(rounds,preferences)
        }
        else
            _gameModel.value = null

        if(gameModel.value != null && isPossibleToStartGame.value!!) {
            val gameJson = Converters.gameModelToJson(gameModel.value!!)
            val game = Game(gameJson)
            Coroutines.io {
                gameRepository.upsertGame(game)
            }
        }

    }
    private fun loadPlayers() {
         Coroutines.ioThenMain({
             playerList = gameRepository.getPlayers().toMutableList()
         }) {
             playerList.forEach { player-> player.isChosen.value = false }
             _players.value = playerList
             observePlayers()
         }
    }
    private fun observePlayers(){
        _players.observeForever{
            val chosenList = it.filter { player -> player.isChosen.value!!  }
            _chosenPlayers.value = chosenList
        }
    }

}
