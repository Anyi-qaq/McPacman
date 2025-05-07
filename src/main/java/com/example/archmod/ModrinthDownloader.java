package com.example.archmod;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;

import com.google.gson.*;

public class ModrinthDownloader {
    public static void showVersionSelection(ServerCommandSource source, String modid) {
        try {
            String gameVersion = source.getServer().getVersion();
            String apiUrl = "https://api.modrinth.com/v2/project/" + modid + "/version";
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "ArchMod/1.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonArray versions = JsonParser.parseString(response.toString()).getAsJsonArray();
            StringBuilder sb = new StringBuilder("可用版本：\n");
            for (JsonElement elem : versions) {
                JsonObject obj = elem.getAsJsonObject();
                String versionNumber = obj.get("version_number").getAsString();
                JsonArray gameVersions = obj.get("game_versions").getAsJsonArray();
                for (JsonElement gv : gameVersions) {
                    if (gv.getAsString().equals(gameVersion)) {
                        sb.append(versionNumber).append(" ");
                    }
                }
            }
            source.sendFeedback(Text.literal(sb.toString()), false);
            // 这里可以扩展为让玩家选择版本后再调用downloadMod
        } catch (Exception e) {
            source.sendFeedback(Text.literal("获取mod版本失败: " + e.getMessage()), false);
        }
    }

    public static void downloadMod(ServerCommandSource source, String modid, String version) {
        try {
            String apiUrl = "https://api.modrinth.com/v2/project/" + modid + "/version/" + version;
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "ArchMod/1.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject versionObj = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray files = versionObj.get("files").getAsJsonArray();
            String downloadUrl = files.get(0).getAsJsonObject().get("url").getAsString();

            // 下载文件
            InputStream is = new URL(downloadUrl).openStream();
            Path modsDir = Paths.get("mods");
            if (!Files.exists(modsDir)) Files.createDirectories(modsDir);
            Path filePath = modsDir.resolve(modid + "-" + version + ".jar");
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            is.close();

            source.sendFeedback(Text.literal("mod已下载并放入mods文件夹: " + filePath.getFileName()), false);
        } catch (Exception e) {
            source.sendFeedback(Text.literal("下载mod失败: " + e.getMessage()), false);
        }
    }
}