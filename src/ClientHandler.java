import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientHandler extends Thread {

    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    final Socket clientSocket;

    public ClientHandler(Socket clientSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        this.clientSocket = clientSocket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run()
    {
        String clientRequest;
        String clientResponse;
        while (true)
        {
            try {

                System.out.print("Connection success with socket : " +  clientSocket.getPort()+ "\n");
                // Ask user what he wants
                dataOutputStream.writeUTF("Connection success");

                // receive the answer from client
                clientRequest = dataInputStream.readUTF();

                if(clientRequest.equals("Exit"))
                {
                    System.out.println("Client " + this.clientSocket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.clientSocket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // write on output stream based on the
                // answer from the client
                switch (clientRequest) {

                    case "Subscribe" :
                        System.out.println("Subscribe request");
                        dataOutputStream.writeUTF("done subscribe");
                        break;

                    case "Publish" :
                        System.out.println("Publish request");
                        dataOutputStream.writeUTF("done publish");
                        break;

                    default:
                        dataOutputStream.writeUTF("Invalid input");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        try
        {
            // closing resources
            this.dataInputStream.close();
            this.dataOutputStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }



}
