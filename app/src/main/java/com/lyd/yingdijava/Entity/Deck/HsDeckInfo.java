package com.lyd.yingdijava.Entity.Deck;

public class HsDeckInfo {
    private int build;

    private int clazz;

    private String clazzCount;

    private String code;

    private int collect;

    private int collectDeckId;

    private String color;

    private int created;

    private String deckImg;

    private String faction;

    private String format;

    private int goldPrice;

    private int hates;

    private int id;  //卡组id，靠这个去get卡组json数据

    private String img;

    private String imgCard;

    private int latestSeries;

    private int likes;

    private String name;

    private int open;

    private int openTime;

    private int pageview;

    private String player;

    private int price;

    private String rank;

    private String rarityInfo;

    private String relatedRes;

    private String remark;

    private int reply;

    private int resource;

    private int score;

    private int sequence;

    private int setId;

    private String setName;

    private String statistic;

    private String tags;

    private int updated;

    private int user;

    private int visible;

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public int getClazz() {
        return clazz;
    }

    public void setClazz(int clazz) {
        this.clazz = clazz;
    }

    public String getClazzCount() {
        return clazzCount;
    }

    public void setClazzCount(String clazzCount) {
        this.clazzCount = clazzCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getCollectDeckId() {
        return collectDeckId;
    }

    public void setCollectDeckId(int collectDeckId) {
        this.collectDeckId = collectDeckId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getDeckImg() {
        return deckImg;
    }

    public void setDeckImg(String deckImg) {
        this.deckImg = deckImg;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public int getHates() {
        return hates;
    }

    public void setHates(int hates) {
        this.hates = hates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgCard() {
        return imgCard;
    }

    public void setImgCard(String imgCard) {
        this.imgCard = imgCard;
    }

    public int getLatestSeries() {
        return latestSeries;
    }

    public void setLatestSeries(int latestSeries) {
        this.latestSeries = latestSeries;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getPageview() {
        return pageview;
    }

    public void setPageview(int pageview) {
        this.pageview = pageview;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRarityInfo() {
        return rarityInfo;
    }

    public void setRarityInfo(String rarityInfo) {
        this.rarityInfo = rarityInfo;
    }

    public String getRelatedRes() {
        return relatedRes;
    }

    public void setRelatedRes(String relatedRes) {
        this.relatedRes = relatedRes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "HSDeck{" +
                "build=" + build +
                ", clazz=" + clazz +
                ", clazzCount='" + clazzCount + '\'' +
                ", code='" + code + '\'' +
                ", collect=" + collect +
                ", collectDeckId=" + collectDeckId +
                ", color='" + color + '\'' +
                ", created=" + created +
                ", deckImg='" + deckImg + '\'' +
                ", faction='" + faction + '\'' +
                ", format='" + format + '\'' +
                ", goldPrice=" + goldPrice +
                ", hates=" + hates +
                ", id=" + id +
                ", img='" + img + '\'' +
                ", imgCard='" + imgCard + '\'' +
                ", latestSeries=" + latestSeries +
                ", likes=" + likes +
                ", name='" + name + '\'' +
                ", open=" + open +
                ", openTime=" + openTime +
                ", pageview=" + pageview +
                ", player='" + player + '\'' +
                ", price=" + price +
                ", rank='" + rank + '\'' +
                ", rarityInfo='" + rarityInfo + '\'' +
                ", relatedRes='" + relatedRes + '\'' +
                ", remark='" + remark + '\'' +
                ", reply=" + reply +
                ", resource=" + resource +
                ", score=" + score +
                ", sequence=" + sequence +
                ", setId=" + setId +
                ", setName='" + setName + '\'' +
                ", statistic='" + statistic + '\'' +
                ", tags='" + tags + '\'' +
                ", updated=" + updated +
                ", user=" + user +
                ", visible=" + visible +
                '}';
    }
}
