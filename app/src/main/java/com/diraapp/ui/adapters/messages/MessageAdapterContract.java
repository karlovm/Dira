package com.diraapp.ui.adapters.messages;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.diraapp.db.entities.Member;
import com.diraapp.db.entities.Room;
import com.diraapp.db.entities.messages.Message;
import com.diraapp.storage.images.WaterfallBalancer;
import com.diraapp.ui.activities.PreparedActivity;
import com.diraapp.ui.adapters.messages.legacy.MessageReplyListener;
import com.diraapp.ui.components.diravideoplayer.DiraVideoPlayer;
import com.diraapp.utils.CacheUtils;

import java.util.HashMap;

/**
 * Data that is used for MessagesAdapter to work properly
 */

public interface MessageAdapterContract {
    WaterfallBalancer getWaterfallBalancer();

    Room getRoom();

    HashMap<String, Member> getMembers();

    CacheUtils getCacheUtils();

    @Deprecated
    Context getContext();

    MessageReplyListener getReplyListener();

    void onFirstMessageScrolled(Message message, int index);

    default void onLastLoadedMessageDisplayed(Message message, int index) {
    }

    PreparedActivity preparePreviewActivity(String filePath, boolean isVideo, Bitmap preview, View transitionSource);

    void attachVideoPlayer(DiraVideoPlayer player);
}
