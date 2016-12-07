package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.data.dao.types.EventsType;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.paperdb.RxPaperBook;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Single;

/**
 * Created by Kosh on 11 Nov 2016, 11:28 AM
 */

public class EventsModel implements Parcelable {
    private final static transient String BOOK_NAME = EventsModel.class.getSimpleName();
    private final static transient String BOOK_KEY = "events";

    @SerializedName("id") private String eventId;
    @SerializedName("type") private EventsType type;
    @SerializedName("actor") private ActorModel actor;
    @SerializedName("repo") private RepoModel repo;
    @SerializedName("payload") private PayloadModel payload;
    @SerializedName("public") private boolean publicX;
    @SerializedName("created_at") private String createdAt;

    public String getEventId() { return eventId;}

    public void setEventId(String eventId) { this.eventId = eventId;}

    public EventsType getType() { return type;}

    public void setType(EventsType type) { this.type = type;}

    public ActorModel getActor() { return actor;}

    public void setActor(ActorModel actor) { this.actor = actor;}

    public RepoModel getRepo() { return repo;}

    public void setRepo(RepoModel repo) { this.repo = repo;}

    public PayloadModel getPayload() { return payload;}

    public void setPayload(PayloadModel payload) { this.payload = payload;}

    public boolean isPublicX() { return publicX;}

    public void setPublicX(boolean publicX) { this.publicX = publicX;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

    @NonNull public static Single<ArrayList<EventsModel>> getEvents() {
        return RxPaperBook.with(BOOK_NAME).read(BOOK_KEY, new ArrayList<>());
    }

    @Nullable public static Observable<EventsModel> getEvent(@NonNull String eventId) {
        return getEvents().toObservable()
                .map(eventsModels -> {
                    if (eventsModels != null && !eventsModels.isEmpty()) {
                        int index = eventsModels.indexOf(new EventsModel(eventId));
                        Logger.e(index, eventsModels.size());
                        if (index != -1) return eventsModels.get(index);
                    }
                    return null;
                });

    }

    public static Observable<EventsModel> save(List<EventsModel> eventsModels) {
        if (eventsModels != null) {
            return RxPaperBook.with(BOOK_NAME).write(BOOK_KEY, eventsModels).toObservable();
        }
        return null;
    }

    public static void delete() {
        RxPaperBook.with(BOOK_NAME).destroy();
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventId);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.actor, flags);
        dest.writeParcelable(this.repo, flags);
        dest.writeParcelable(this.payload, flags);
        dest.writeByte(this.publicX ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
    }

    public EventsModel() {}

    public EventsModel(String eventId) {
        this.eventId = eventId;
    }

    protected EventsModel(Parcel in) {
        this.eventId = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : EventsType.values()[tmpType];
        this.actor = in.readParcelable(ActorModel.class.getClassLoader());
        this.repo = in.readParcelable(RepoModel.class.getClassLoader());
        this.payload = in.readParcelable(PayloadModel.class.getClassLoader());
        this.publicX = in.readByte() != 0;
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<EventsModel> CREATOR = new Parcelable.Creator<EventsModel>() {
        @Override public EventsModel createFromParcel(Parcel source) {return new EventsModel(source);}

        @Override public EventsModel[] newArray(int size) {return new EventsModel[size];}
    };

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsModel that = (EventsModel) o;

        return eventId != null ? eventId.equals(that.eventId) : that.eventId == null;

    }

    @Override public int hashCode() {
        return eventId != null ? eventId.hashCode() : 0;
    }
}
