package com.diraapp.ui.activities.room;

import com.diraapp.db.entities.Attachment;
import com.diraapp.db.entities.AttachmentType;
import com.diraapp.ui.bottomsheet.filepicker.SelectorFileInfo;

import java.util.ArrayList;
import java.util.List;

public class MultiAttachmentLoader {

    private String messageText;
    private List<Attachment> attachments = new ArrayList<>();

    private RoomActivityContract.Presenter presenter;

    private int readySize = -1;
    private int uploadedCount = 0;


    public MultiAttachmentLoader(String messageText, RoomActivityContract.Presenter roomActivityPresenter) {
        this.messageText = messageText;
        this.presenter = roomActivityPresenter;
    }

    public void send(List<SelectorFileInfo> selectorFileInfoList) {
        readySize = selectorFileInfoList.size();

        for (int i = 0; i < readySize; i++) {
            attachments.add(null);
        }

        int i = 0;
        for (SelectorFileInfo selectorFileInfo : selectorFileInfoList) {
            AttachmentType attachmentType = AttachmentType.VIDEO;
            if (!selectorFileInfo.isVideo()) {
                attachmentType = AttachmentType.IMAGE;
            }
            int currentI = i;
            presenter.uploadAttachment(attachmentType, new RoomActivityPresenter.AttachmentReadyListener() {
                        @Override
                        public void onReady(Attachment attachment) {
                            attachments.set(currentI, attachment);
                            uploadedCount++;
                            if (uploadedCount == readySize) {
                                presenter.sendMessage((ArrayList<Attachment>) attachments, messageText);
                            }
                        }
                    },
                    selectorFileInfo.getFilePath());
            i++;
        }
    }


}
