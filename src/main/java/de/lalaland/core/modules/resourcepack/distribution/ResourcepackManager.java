package de.lalaland.core.modules.resourcepack.distribution;

import de.lalaland.core.CorePlugin;
import de.lalaland.core.modules.resourcepack.distribution.ResourcepackServer.ResourceServerConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResourcepackManager {

  private final int port = 9555;
  private final String host = "127.0.0.1"; //CHANGEME?! D:
  private final String hash;

  private ResourcepackServer server;
  private final File pack;

  public ResourcepackManager(final CorePlugin plugin) {
    pack = new File(plugin.getDataFolder(), "serverpack.zip");
    //Start the HTTP Server.
    startServer();

    hash = getFileHashChecksum(pack);
  }

  public String getResourceHash() {
    return hash;
  }

  public String getDownloadURL(final String packname) {
    return "http://" + host + ":" + port + "/" + packname;
  }

  public void shutdown() {
    server.terminate();
  }


  private void startServer() {
    try {
      server = new ResourcepackServer(port) {

        @Override
        public File requestFileCallback(final ResourceServerConnection connection,
            final String request) {
          final Player player = getAddress(connection);

          if (player == null) {
            // Connection from unknown IP, refuse connection.
            return null;
          }
          Bukkit.getLogger().info(
              "Connection " + connection.getClient().getInetAddress() + " is requesting + "
                  + request);
          // Return the .zip file
          return pack;
        }

        @Override
        public void onSuccessfulRequest(final ResourceServerConnection connection,
            final String request) {
          Bukkit.getLogger().info(
              "Successfully served " + request + " to " + connection.getClient().getInetAddress()
                  .getHostAddress());
        }

        @Override
        public void onClientRequest(final ResourceServerConnection connection,
            final String request) {
          Bukkit.getLogger()
              .info(connection.getClient().getInetAddress() + " is requesting the resourcepack");
        }

        @Override
        public void onRequestError(final ResourceServerConnection connection, final int code) {
          Bukkit.getLogger().info(
              "Error " + code + " while attempting to serve " + connection.getClient()
                  .getInetAddress().getHostAddress());
        }
      };

      server.start();
      Bukkit.getLogger().info("Successfully started the HTTP Server");
    } catch (final IOException ex) {
      Bukkit.getLogger().severe("Failed to start HTTP ResourceServer!");
      ex.printStackTrace();
    }
  }

  private String getFileHashChecksum(final File input) {
    try (final InputStream in = new FileInputStream(input)) {
      final MessageDigest digest = MessageDigest.getInstance("SHA1");
      final byte[] block = new byte[2048];
      int length;

      while ((length = in.read(block)) > 0) {
        digest.update(block, 0, length);
      }

      final byte[] bytes = digest.digest();
      final String hash = String.format("%040x", new BigInteger(1, bytes));
      return hash;
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  private Player getAddress(final ResourceServerConnection connection) {
    final byte[] ip = connection.getClient().getInetAddress().getAddress();

    for (final Player player : Bukkit.getOnlinePlayers()) {
      if (Arrays.equals(player.getAddress().getAddress().getAddress(), ip)) {
        return player;
      }
    }
    return null;
  }


}
