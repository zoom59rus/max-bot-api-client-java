package ru.max.botapi.server;

import ru.max.botapi.client.impl.JacksonSerializer;
import spark.Spark;

import static spark.Spark.*;


public class MaxServer {
    public static final String ENDPOINT = "http://localhost:4567";

    public static void start() {
        JacksonSerializer mapper = new JacksonSerializer();
        MaxService service = new MaxService(mapper);
        before(((request, response) -> {
            boolean isUpload = request.pathInfo().startsWith("/fileupload")
                    || request.pathInfo().startsWith("/imageupload")
                    || request.pathInfo().startsWith("/avupload");

            if (isUpload) {
                return;
            }

            String accessToken = request.queryParams("access_token");
            if (!MaxService.ACCESS_TOKEN.equals(accessToken)) {
                halt(401, "Invalid access token.");
            }
        }));

        get("/chats", service::getChats, service::serialize);
        get("/chats/:chatId", service::getChat, service::serialize);
        patch("/chats/:chatId", service::editChat, service::serialize);
        delete("/chats/:chatId", service::deleteChat, service::serialize);
        get("/chats/:chatId/members", service::getMembers, service::serialize);
        post("/chats/:chatId/members", service::addMembers, service::serialize);
        delete("/chats/:chatId/members", service::removeMembers, service::serialize);
        delete("/chats/:chatId/members/me", service::leaveChat, service::serialize);
        get("/chats/:chatId/members/admins", service::getAdmins, service::serialize);
        post("/chats/:chatId/members/admins", service::postAdmins, service::serialize);
        delete("/chats/:chatId/members/admins/:userId", service::deleteAdmin, service::serialize);
        post("/chats/:chatId/actions", service::sendAction, service::serialize);
        get("/chats/:chatId/pin", service::getPinnedMessage, service::serialize);
        put("/chats/:chatId/pin", service::pinMessage, service::serialize);
        delete("/chats/:chatId/pin", service::unpinMessage, service::serialize);
        put("/messages", service::editMessage, service::serialize);
        get("/messages/:messageId", service::getMessage, service::serialize);
        post("/answers", service::answer, service::serialize);
        get("/subscriptions", service::getSubscriptions, service::serialize);
        post("/subscriptions", service::addSubscription, service::serialize);
        delete("/subscriptions", service::removeSubscription, service::serialize);
        post("/uploads", service::getUploadUrl, service::serialize);
        get("/videos/:token", service::getVideoDetails, service::serialize);

        awaitInitialization();
    }

    public static void stop() {
        Spark.stop();
        awaitInitialization();
    }
}
