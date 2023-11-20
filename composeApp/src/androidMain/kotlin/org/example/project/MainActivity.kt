package org.example.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    setContent {
      App()
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App()
}

//@Preview
//@Composable
//fun UserListItem(user: User = User("One")){
//  Surface(modifier = Modifier.fillMaxWidth(1f)) {
//    Column(modifier = Modifier.padding(8.dp)) {
//      Text(text = user.name)
//      Row {
//        Text(text = "${user.userStatus}")
//        Text(text = "${user.channelOperation}", modifier = Modifier.padding(16.dp, 0.dp,0.dp,0.dp))
//      }
//    }
//  }
//}
//
//enum class UserStatus {
//  SAME_GROUP,
//  ANOTHER_GROUP,
//  IN_1_ON_1_CALL,
//  ONLINE,
//  OFFLINE
//}
//
//enum class ChannelOperation {
//  JOIN, LEAVE, ACTIVE, SUSPEND
//}
//
//class User(actualName: String) {
//  var name: String = actualName
//  var userStatus: UserStatus = UserStatus.OFFLINE
//  var channelOperation: ChannelOperation = ChannelOperation.LEAVE
//}
//
//@Preview
//@Composable
//fun UserList(users: List<User> = mutableListOf()){
//  Surface(modifier = Modifier.fillMaxSize(1f).padding(8.dp)) {
//    LazyColumn {
//      item {
//        Text(text = "Joined")
//      }
//      items(users) {
//        UserListItem(user = it)
//        Divider(color = Color.LightGray)
//      }
//      item {
//        Text(text = "Not Joined")
//      }
//      items(users) {
//        UserListItem(user = it)
//        Divider(color = Color.LightGray)
//      }
//    }
//  }
//}