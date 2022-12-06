package com.example.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button buttonUp;
    private Button buttonDown;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonEnter;
    private Button buttonServer;
    private TextView ipOut;
    private TextInputEditText ipIn;
    private TextView portOut;
    private TextInputEditText portIn;
    private Mqtt5BlockingClient client;
    private String serverIpTxt = "0.0.0.0";
    private String serverPortTxt = "8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUp = (Button) findViewById(R.id.buttonUp);
        buttonDown = (Button) findViewById(R.id.buttonDown);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonEnter = (Button) findViewById(R.id.buttonEnter);
        buttonServer = (Button) findViewById(R.id.buttonServer);
        ipOut = (TextView) findViewById(R.id.ipOutput);
        portOut = (TextView) findViewById(R.id.portOutput);
        ipIn = (TextInputEditText) findViewById(R.id.ipInput);
        portIn = (TextInputEditText) findViewById(R.id.portInput);

        buttonServer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                serverIpTxt = String.valueOf(ipIn.getText());
                ipOut.setText("Server IP: " + serverIpTxt);
                serverPortTxt = String.valueOf(portIn.getText());
                portOut.setText("Server Port: " + serverPortTxt);
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    client = Mqtt5Client.builder()
                            .identifier(UUID.randomUUID().toString())
                            .serverHost(serverIpTxt)
                            .buildBlocking();
                    client.connect();

                    String message = "up";

                    client.publishWith().topic("gol-comm").qos(MqttQos.EXACTLY_ONCE).payload(message.getBytes()).send();
                    client.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error in Connection", Toast.LENGTH_SHORT).show());
                }

                // start the Thread to connect to server
//                new Thread(new ClientThread(message, serverIpTxt, Integer.parseInt(serverPortTxt))).start();
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    client = Mqtt5Client.builder()
                            .identifier(UUID.randomUUID().toString())
                            .serverHost(serverIpTxt)
                            .buildBlocking();
                    client.connect();

                    String message = "down";

                    client.publishWith().topic("gol-comm").qos(MqttQos.AT_MOST_ONCE).payload(message.getBytes()).send();
                    client.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error in Connection", Toast.LENGTH_SHORT).show());
                }

                // start the Thread to connect to server
//                new Thread(new ClientThread(message, serverIpTxt, Integer.parseInt(serverPortTxt))).start();
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = Mqtt5Client.builder()
                        .identifier(UUID.randomUUID().toString())
                        .serverHost(serverIpTxt)
                        .buildBlocking();
                client.connect();

                String message = "right";

                client.publishWith().topic("gol-comm").qos(MqttQos.EXACTLY_ONCE).payload(message.getBytes()).send();
                client.disconnect();

                // start the Thread to connect to server
//                new Thread(new ClientThread(message, serverIpTxt, Integer.parseInt(serverPortTxt))).start();
            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = Mqtt5Client.builder()
                        .identifier(UUID.randomUUID().toString())
                        .serverHost(serverIpTxt)
                        .buildBlocking();
                client.connect();

                String message = "left";

                client.publishWith().topic("gol-comm").qos(MqttQos.EXACTLY_ONCE).payload(message.getBytes()).send();
                client.disconnect();

                // start the Thread to connect to server
//                new Thread(new ClientThread(message, serverIpTxt, Integer.parseInt(serverPortTxt))).start();
            }
        });
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = Mqtt5Client.builder()
                        .identifier(UUID.randomUUID().toString())
                        .serverHost(serverIpTxt)
                        .buildBlocking();
                client.connect();

                String message = "enter";

                client.publishWith().topic("gol-comm").qos(MqttQos.EXACTLY_ONCE).payload(message.getBytes()).send();
                client.disconnect();

                // start the Thread to connect to server
//                new Thread(new ClientThread(message, serverIpTxt, Integer.parseInt(serverPortTxt))).start();
            }
        });
    }

    // the ClientThread class performs
    // the networking operations
    class ClientThread implements Runnable {
        private final String message;
        private final String IP;
        private final int PORT;

        ClientThread(String message, String IP, int PORT) {
            this.message = message;
            this.IP = IP;
            this.PORT = PORT;
        }
        @Override
        public void run() {
            try {
                // the IP and port should be correct to have a connection established
                // Creates a stream socket and connects it to the specified port number on the named host.
                Socket client = new Socket(this.IP, this.PORT);  // connect to server
//                if(client.isClosed()) {
//
//                }
                PrintWriter printwriter = new PrintWriter(client.getOutputStream(),true);
                printwriter.write(message);  // write the message to output stream
                printwriter.flush();
                printwriter.close();

                // closing the connection
                client.close();

            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error in Socket Connection", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }

            // updating the UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    textField.setText("");
                }
            });
        }
    }
}