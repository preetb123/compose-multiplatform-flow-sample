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
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
  val log = logging()

  var usersFlow = MutableStateFlow<User?>(null)
  var usersState = remember { mutableStateListOf<User>().apply {
    repeat(50) {
      val name = getRandomName()
      val user = User(it, name)
      add(it, user)
    }
  }}

  LaunchedEffect("COUNTER") {
    launch {
      while (true) {
        val userId = (0..49).random()
        val randomUserStatus = (0..4).random()
        val user = usersState[userId]
        val newStatus = UserStatus.entries[randomUserStatus]
        user?.userStatus = newStatus
        usersFlow.emit(user)
        delay(100)
      }
    }
    launch {
      while (true) {
        val userId = (0..49).random()
        val randomUserStatus = (0..3).random()
        val user = usersState[userId]
        user?.channelOperation = ChannelOperation.entries[randomUserStatus]
        usersFlow.emit(user)
        delay(100)
      }
    }

    launch {
      usersFlow.collect {
        usersState.remove(it)
        usersState.add(it!!)
      }
    }
  }

  MaterialTheme {
    Column {
      UserList(usersState)
    }
  }
}

@Composable
fun UserListItem(user: User) {
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

class User(
  var id: Int = 0,
  var name: String = getRandomName(),
  var userStatus: UserStatus = UserStatus.OFFLINE,
  var channelOperation: ChannelOperation = ChannelOperation.LEAVE
)

@Composable
fun UserList(users: List<User>) {
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
        if(it.userStatus != UserStatus.SAME_GROUP) {
          UserListItem(user = it)
          Divider(color = Color.LightGray)
        }
      }
    }
  }
}

val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
fun getRandomName() = List(10) { charPool.random() }.joinToString("")
