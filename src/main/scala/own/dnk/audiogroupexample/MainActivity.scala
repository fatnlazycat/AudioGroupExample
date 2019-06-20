package own.dnk.audiogroupexample

import java.net.InetAddress

import android.content.Context
import android.media.AudioManager
import android.net.rtp.{AudioCodec, AudioGroup, AudioStream, RtpStream}
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.Formatter
import android.util.Log
import android.view.{MotionEvent, View}
import android.view.View.{OnClickListener, OnTouchListener}

class MainActivity extends AppCompatActivity with TypedFindView {
  val TAG = "MainActivity"
  lazy val tvPort = findView(TR.etPort)

  lazy val audioManager  =  getSystemService(Context.AUDIO_SERVICE).asInstanceOf[AudioManager]
  lazy val audioGroup = new AudioGroup()
  var audioStreamIn: AudioStream = _

  var remoteAddress: String = _
  var remotePort: String = _

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.main)

    val wm = getSystemService(Context.WIFI_SERVICE).asInstanceOf[WifiManager]
    val ipString = Formatter.formatIpAddress(wm.getConnectionInfo.getIpAddress)

    val tvIPAddress = findView(TR.tvIPAddress)
    tvIPAddress.setText(ipString)

    val btnConnect = findView(TR.btnConnect)
    /*btnConnect.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = {
        remoteAddress = tvIPAddress.getText.toString
        remotePort = tvPort.getText.toString
      }
    })*/

    btnConnect.setOnTouchListener(new OnTouchListener {
      override def onTouch(v: View, event: MotionEvent): Boolean = {
        remoteAddress = tvIPAddress.getText.toString
        remotePort = tvPort.getText.toString

        event.getAction match  {
          case MotionEvent.ACTION_DOWN => {
            switchSpeakListen(modeListen = false)
            true
          }
          case MotionEvent.ACTION_UP => {
            switchSpeakListen(modeListen = true)
            true
          }
          case _ => false
        }
      }
    })

    initAudio(ipString)
  }

  def initAudio(myAddress: String): Unit = {
    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION)
    audioGroup.setMode(AudioGroup.MODE_NORMAL)

    audioStreamIn = new AudioStream(InetAddress.getByName(myAddress))
    /*audioStreamIn.setCodec(AudioCodec.PCMU)
    audioStreamIn.setMode(RtpStream.MODE_NORMAL)*/

    showPort(audioStreamIn.getLocalPort.toString)
  }

  def showPort(port: String): Unit ={
    tvPort.setHint(port)
  }

  /*def doAudioJob(remoteAddress: String, remotePort: String): Unit = {
    try {
      audioStreamIn.join(null)
      audioStreamIn.setCodec(AudioCodec.PCMU)
      audioStreamIn.associate(InetAddress.getByName(remoteAddress), Integer.parseInt(remotePort))
      audioStreamIn.join(audioGroup)
    }
    catch {
      case e: Exception => Log.e(TAG, "error: ", e)
    }
  }*/

  def switchSpeakListen(modeListen: Boolean): Unit = {
    audioManager.setSpeakerphoneOn(modeListen)
    audioManager.setMicrophoneMute(modeListen)

    audioStreamIn.join(null)
    audioStreamIn.setCodec(AudioCodec.PCMU)
    audioStreamIn.setMode(if (modeListen) RtpStream.MODE_RECEIVE_ONLY else RtpStream.MODE_SEND_ONLY)
    audioStreamIn.associate(InetAddress.getByName(remoteAddress), Integer.parseInt(remotePort))
    audioStreamIn.join(audioGroup)

    Log.d(TAG, s"audioStream $audioStreamIn joined audioGroup $audioGroup")

  }
}