/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.core.ui.components;

import jakarta.validation.constraints.NotNull;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.*;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import de.kaiserpfalzedv.core.ui.i18n.TranslatableComponent;
import de.paladinsinn.tp.dcis.data.HasAvatar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.vaadin.flow.component.Unit.PIXELS;

/**
 * Avatar -- A component for editing an avatar.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.1.0  2021-04-07
 */
@SuppressWarnings("unused")
@Slf4j
public class Avatar extends Span implements HasSize, LocaleChangeObserver, TranslatableComponent {
    private final String i18nPrefix;
    private final Image avatar = new Image();
    private final MemoryBuffer avatarBuffer = new MemoryBuffer();
    private final Upload avatarUpload = new Upload(avatarBuffer);
    private Locale locale;


    public Avatar(
            @NotNull final String i18nPrefix,

            @NotNull final HasAvatar data,
            @NotNull final int minWidth,
            @NotNull final int maxWidth,

            @NotNull final int minHeight,
            @NotNull final int maxHeight,

            @NotNull final int maxFileSize
    ) {
        this.i18nPrefix = i18nPrefix;

        avatar.setMinWidth(minWidth, PIXELS);
        avatar.setMaxWidth(maxWidth, PIXELS);

        avatar.setMinHeight(minHeight, PIXELS);
        avatar.setMaxHeight(maxHeight, PIXELS);

        avatarUpload.setMaxFiles(1);
        avatarUpload.setMaxFileSize(maxFileSize);
        avatarUpload.setDropAllowed(true);
        avatarUpload.setAcceptedFileTypes("image/png", "image/jpg", "image/jpeg", "image/gif");
        avatarUpload.addFinishedListener(e -> {
            try {
                data.setAvatar(avatarBuffer.getInputStream());

                Notification.show(
                        getTranslation("input.upload.success", avatarBuffer.getFileName()),
                        1000,
                        Notification.Position.BOTTOM_STRETCH
                );
            } catch (IOException ioException) {
                log.error(
                        "Upload of the avatar failed. file='{}', type='{}'",
                        avatarBuffer.getFileData(), avatarBuffer.getFileData().getMimeType()
                );
                Notification.show(
                        getTranslation("input.upload.failed", ioException.getLocalizedMessage()),
                        2000,
                        Notification.Position.BOTTOM_STRETCH
                );
            }
        });

        locale = VaadinSession.getCurrent().getLocale();
    }


    public void setValue(@NotNull final HasAvatar data) {
        if (data.getAvatar() != null)
            avatar.setSrc(data.getAvatar());
    }

    @Override
    public void translate() {
        removeAll();

        avatar.setTitle(getTranslation(String.format("%s.%s", i18nPrefix, "caption")));
        avatarUpload.setDropLabelIcon(avatar);

        add(avatarUpload);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        setLocale(event.getLocale());
    }

    @Override
    public void setLocale(Locale locale) {
        if (this.locale != null && this.locale.equals(locale)) {
            log.debug("locale did not change - ignoring event. old={}, new={}", this.locale, locale);
            return;
        }

        this.locale = locale;

        translate();
    }

    public Registration addAllFinishedListener(ComponentEventListener<AllFinishedEvent> listener) {
        return avatarUpload.addAllFinishedListener(listener);
    }

    public int getMaxFiles() {
        return avatarUpload.getMaxFiles();
    }

    public void setMaxFiles(int maxFiles) {
        avatarUpload.setMaxFiles(maxFiles);
    }

    public int getMaxFileSize() {
        return avatarUpload.getMaxFileSize();
    }

    public void setMaxFileSize(int maxFileSize) {
        avatarUpload.setMaxFileSize(maxFileSize);
    }

    public boolean isAutoUpload() {
        return avatarUpload.isAutoUpload();
    }

    public void setAutoUpload(boolean autoUpload) {
        avatarUpload.setAutoUpload(autoUpload);
    }

    public boolean isDropAllowed() {
        return avatarUpload.isDropAllowed();
    }

    public void setDropAllowed(boolean dropAllowed) {
        avatarUpload.setDropAllowed(dropAllowed);
    }

    public List<String> getAcceptedFileTypes() {
        return avatarUpload.getAcceptedFileTypes();
    }

    public void setAcceptedFileTypes(String... acceptedFileTypes) {
        avatarUpload.setAcceptedFileTypes(acceptedFileTypes);
    }

    public Component getUploadButton() {
        return avatarUpload.getUploadButton();
    }

    public void setUploadButton(Component uploadButton) {
        avatarUpload.setUploadButton(uploadButton);
    }

    public Component getDropLabel() {
        return avatarUpload.getDropLabel();
    }

    public void setDropLabel(Component dropLabel) {
        avatarUpload.setDropLabel(dropLabel);
    }

    public Component getDropLabelIcon() {
        return avatarUpload.getDropLabelIcon();
    }

    public void setDropLabelIcon(Component dropLabelIcon) {
        avatarUpload.setDropLabelIcon(dropLabelIcon);
    }

    public void interruptUpload() {
        avatarUpload.interruptUpload();
    }

    public boolean isUploading() {
        return avatarUpload.isUploading();
    }

    public Registration addProgressListener(ComponentEventListener<ProgressUpdateEvent> listener) {
        return avatarUpload.addProgressListener(listener);
    }

    public Registration addFailedListener(ComponentEventListener<FailedEvent> listener) {
        return avatarUpload.addFailedListener(listener);
    }

    public Registration addFinishedListener(ComponentEventListener<FinishedEvent> listener) {
        return avatarUpload.addFinishedListener(listener);
    }

    public Registration addStartedListener(ComponentEventListener<StartedEvent> listener) {
        return avatarUpload.addStartedListener(listener);
    }

    public Registration addSucceededListener(ComponentEventListener<SucceededEvent> listener) {
        return avatarUpload.addSucceededListener(listener);
    }

    public Registration addFileRejectedListener(ComponentEventListener<FileRejectedEvent> listener) {
        return avatarUpload.addFileRejectedListener(listener);
    }

    public Receiver getReceiver() {
        return avatarUpload.getReceiver();
    }

    public void setReceiver(Receiver receiver) {
        avatarUpload.setReceiver(receiver);
    }

    public UploadI18N getI18n() {
        return avatarUpload.getI18n();
    }

    public void setI18n(UploadI18N i18n) {
        avatarUpload.setI18n(i18n);
    }
}
