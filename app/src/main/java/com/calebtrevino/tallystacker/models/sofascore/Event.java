package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customId")
    @Expose
    private String customId;
    @SerializedName("homeScore")
    @Expose
    private TeamScore homeScore;
    @SerializedName("awayScore")
    @Expose
    private TeamScore awayScore;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("winnerCode")
    @Expose
    private Integer winnerCode;
    @SerializedName("changes")
    @Expose
    private Changes changes;
    @SerializedName("roundInfo")
    @Expose
    private RoundInfo roundInfo;
    @SerializedName("sport")
    @Expose
    private Sport sport;
    @SerializedName("homeTeam")
    @Expose
    private Team homeTeam;
    @SerializedName("awayTeam")
    @Expose
    private Team awayTeam;
    @SerializedName("hasHighlights")
    @Expose
    private Boolean hasHighlights;
    @SerializedName("hasHighlightsStream")
    @Expose
    private Boolean hasHighlightsStream;
    @SerializedName("homeRedCards")
    @Expose
    private Integer homeRedCards;
    @SerializedName("awayRedCards")
    @Expose
    private Integer awayRedCards;
    @SerializedName("statusDescription")
    @Expose
    private String statusDescription;
    @SerializedName("hasLiveForm")
    @Expose
    private Boolean hasLiveForm;
    @SerializedName("uniqueTournamentId")
    @Expose
    private Integer uniqueTournamentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("formatedStartDate")
    @Expose
    private String formatedStartDate;
    @SerializedName("startTimestamp")
    @Expose
    private Integer startTimestamp;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("hasLineupsList")
    @Expose
    private Boolean hasLineupsList;
    @SerializedName("hasOdds")
    @Expose
    private Boolean hasOdds;
    @SerializedName("hasLiveOdds")
    @Expose
    private Boolean hasLiveOdds;
    @SerializedName("hasFirstToServe")
    @Expose
    private Boolean hasFirstToServe;
    @SerializedName("hasDraw")
    @Expose
    private Boolean hasDraw;
    @SerializedName("isSyncable")
    @Expose
    private Boolean isSyncable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public TeamScore getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(TeamScore homeScore) {
        this.homeScore = homeScore;
    }

    public TeamScore getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(TeamScore awayScore) {
        this.awayScore = awayScore;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getWinnerCode() {
        return winnerCode;
    }

    public void setWinnerCode(Integer winnerCode) {
        this.winnerCode = winnerCode;
    }

    public Changes getChanges() {
        return changes;
    }

    public void setChanges(Changes changes) {
        this.changes = changes;
    }

    public RoundInfo getRoundInfo() {
        return roundInfo;
    }

    public void setRoundInfo(RoundInfo roundInfo) {
        this.roundInfo = roundInfo;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Boolean getHasHighlights() {
        return hasHighlights;
    }

    public void setHasHighlights(Boolean hasHighlights) {
        this.hasHighlights = hasHighlights;
    }

    public Boolean getHasHighlightsStream() {
        return hasHighlightsStream;
    }

    public void setHasHighlightsStream(Boolean hasHighlightsStream) {
        this.hasHighlightsStream = hasHighlightsStream;
    }

    public Integer getHomeRedCards() {
        return homeRedCards;
    }

    public void setHomeRedCards(Integer homeRedCards) {
        this.homeRedCards = homeRedCards;
    }

    public Integer getAwayRedCards() {
        return awayRedCards;
    }

    public void setAwayRedCards(Integer awayRedCards) {
        this.awayRedCards = awayRedCards;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Boolean getHasLiveForm() {
        return hasLiveForm;
    }

    public void setHasLiveForm(Boolean hasLiveForm) {
        this.hasLiveForm = hasLiveForm;
    }

    public Integer getUniqueTournamentId() {
        return uniqueTournamentId;
    }

    public void setUniqueTournamentId(Integer uniqueTournamentId) {
        this.uniqueTournamentId = uniqueTournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFormatedStartDate() {
        return formatedStartDate;
    }

    public void setFormatedStartDate(String formatedStartDate) {
        this.formatedStartDate = formatedStartDate;
    }

    public Integer getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Integer startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getHasLineupsList() {
        return hasLineupsList;
    }

    public void setHasLineupsList(Boolean hasLineupsList) {
        this.hasLineupsList = hasLineupsList;
    }

    public Boolean getHasOdds() {
        return hasOdds;
    }

    public void setHasOdds(Boolean hasOdds) {
        this.hasOdds = hasOdds;
    }

    public Boolean getHasLiveOdds() {
        return hasLiveOdds;
    }

    public void setHasLiveOdds(Boolean hasLiveOdds) {
        this.hasLiveOdds = hasLiveOdds;
    }

    public Boolean getHasFirstToServe() {
        return hasFirstToServe;
    }

    public void setHasFirstToServe(Boolean hasFirstToServe) {
        this.hasFirstToServe = hasFirstToServe;
    }

    public Boolean getHasDraw() {
        return hasDraw;
    }

    public void setHasDraw(Boolean hasDraw) {
        this.hasDraw = hasDraw;
    }

    public Boolean getIsSyncable() {
        return isSyncable;
    }

    public void setIsSyncable(Boolean isSyncable) {
        this.isSyncable = isSyncable;
    }

    @Override
    public String toString() {
        return "Event{" +
                "homeTeam=" + homeTeam + " " + homeScore.getCurrent() + " " + homeTeam.getId() +
                ", awayTeam=" + awayTeam + " " + awayScore.getCurrent() + " " + awayTeam.getId() +
                ", status " + status.getCode() + " " + status.getType() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!id.equals(event.id)) return false;
        if (!homeTeam.equals(event.homeTeam)) return false;
        if (!awayTeam.equals(event.awayTeam)) return false;
        return uniqueTournamentId.equals(event.uniqueTournamentId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + homeTeam.hashCode();
        result = 31 * result + awayTeam.hashCode();
        result = 31 * result + uniqueTournamentId.hashCode();
        return result;
    }
}
