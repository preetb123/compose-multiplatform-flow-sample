import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.lighthousegames.logging.logging
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
  val log = logging()
  var counter = MutableStateFlow(0)
  var statusFlow = MutableStateFlow(Pair<String, String>("status", UserStatus.OFFLINE.name))
  var channelFlow = MutableStateFlow(Pair<String, String>("channel", ChannelOperation.LEAVE.name))

  var listFlow = MutableStateFlow(buildList {
    val users = arrayListOf<User>()
    repeat(40) {
      val name = getRandomName()
      val user = User(name)
      users.add(it, user)
    }
    addAll(users)
  })

  var data by remember { mutableStateOf(0) }
  var userList by remember { mutableStateOf<ArrayList<User>>(arrayListOf<User>()) }

  LaunchedEffect("COUNTER") {
    launch {
      while (true) {
        counter.emit(counter.value.plus(2))
        delay(10)
      }
    }

    launch {
      while (true) {
        counter.emit(counter.value.dec())
        delay(10)
      }
    }

    launch {
      counter.collect {
        data = it
      }
    }

    launch {
      while (true) {
        val userId = (0..39).random()
        val randomUserStatus = (0..4).random()
        var users = arrayListOf<User>()
        users.addAll(listFlow.value)
        val user = users[userId]
        user?.userStatus = UserStatus.entries[randomUserStatus]
        users.set(userId, user)
        statusFlow.emit(Pair("status", UserStatus.entries[randomUserStatus].name))
        delay(20)
      }
    }
//
    launch {
      while (true) {
        val userId = (0..39).random()
        val randomUserStatus = (0..3).random()
        var users = arrayListOf<User>()
        users.addAll(listFlow.value)
        val user = users[userId]
        user?.channelOperation = ChannelOperation.entries[randomUserStatus]
        users.set(userId, user)
        channelFlow.emit(Pair("channel", ChannelOperation.entries[randomUserStatus].name))
        delay(40)
      }
    }
//
    launch {
      log.d { "received in launch" }
      listFlow.collect {
        log.d { "collecting list" }
        userList = arrayListOf(*it.toTypedArray())
      }
    }
    launch {
      channelFlow.collect {
        log.d { "collecting channel" }
        val userId = (0..39).random()
        var users = arrayListOf<User>()
        users.addAll(listFlow.value)
        val user = users[userId]
        user?.channelOperation = ChannelOperation.valueOf(it.second)
        users.set(userId, user)
        userList = arrayListOf(*users.toTypedArray())
      }
    }

    launch {
      statusFlow.collect {
        log.d { "collecting status" }
        val userId = (0..39).random()
        var users = arrayListOf<User>()
        users.addAll(listFlow.value)
        val user = users[userId]
        user?.userStatus = UserStatus.valueOf(it.second)
        users.set(userId, user)
        userList = users
      }
    }
  }

  MaterialTheme {
    Column {
      Text(text = "Counter: $data", modifier = Modifier.padding(8.dp))
      UserList(userList)
    }
  }
}

@Composable
fun UserListItem(user: User = User("One")) {
  Surface(modifier = Modifier.fillMaxWidth(1f)) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text(text = user.name)
      Row {
        Text(text = "${user.userStatus}")
        Text(
          text = "${user.channelOperation}",
          modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp)
        )
      }
    }
  }
}

enum class UserStatus {
  SAME_GROUP,
  ANOTHER_GROUP,
  IN_1_ON_1_CALL,
  ONLINE,
  OFFLINE
}

enum class ChannelOperation {
  JOIN, LEAVE, ACTIVE, SUSPEND
}

class User(actualName: String) : Comparable<User>{
  var id: Int = 0
  var name: String = actualName
  var userStatus: UserStatus = UserStatus.OFFLINE
  var channelOperation: ChannelOperation = ChannelOperation.LEAVE

  override fun compareTo(other: User): Int {
    TODO("Not yet implemented")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as User

    if (id != other.id) return false
    if (name != other.name) return false
    if (userStatus != other.userStatus) return false
    if (channelOperation != other.channelOperation) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + userStatus.hashCode()
    result = 31 * result + channelOperation.hashCode()
    return result
  }
}

@Composable
fun UserList(users: List<User> = mutableListOf()) {
  Surface(modifier = Modifier.fillMaxSize(1f).padding(8.dp)) {
    LazyColumn {
      item {
        Text(
          text = "Joined",
          modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp),
          style = TextStyle(fontWeight = FontWeight.Bold)
        )
      }
      items(users) {
        if (it.userStatus == UserStatus.SAME_GROUP) {
          UserListItem(user = it)
          Divider(color = Color.LightGray)
        }
      }
      item {
        Text(
          text = "Not Joined",
          modifier = Modifier.padding(0.dp, 18.dp, 0.dp, 0.dp),
          style = TextStyle(fontWeight = FontWeight.Bold)
        )
      }
      items(users) {
        UserListItem(user = it)
        Divider(color = Color.LightGray)
      }
    }
  }
}

val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun getRandomName() = List(10) { charPool.random() }.joinToString("")
