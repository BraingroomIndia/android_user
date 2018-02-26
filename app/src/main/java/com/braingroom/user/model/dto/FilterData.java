package com.braingroom.user.model.dto;

import static android.text.TextUtils.isEmpty;

import com.braingroom.user.model.request.GeneralFilterReq;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Getter;
import lombok.NonNull;


/*
 * Created by agrahari on 07/04/17.
 */
@Getter
public class FilterData implements Serializable {

    private HashMap<String, Integer> categoryFilterMap = new HashMap<>();
    private HashMap<String, Integer> segmentsFilterMap = new HashMap<>();
    private HashMap<String, Integer> cityFilterMap = new HashMap<>();
    private HashMap<String, Integer> localityFilterMap = new HashMap<>();
    private HashMap<String, Integer> communityFilterMap = new HashMap<>();
    private HashMap<String, Integer> classTypeFilterMap = new HashMap<>();
    private HashMap<String, Integer> classScheduleFilterMap = new HashMap<>();
    private HashMap<String, Integer> vendorFilterMap = new HashMap<>();
    private HashMap<String, Integer> classDurationFilterMap = new HashMap<>();
    //    1 Category
    private Integer categoryId;
    //    2 Segment
    private Integer segmentId;
    //    3 City
    private Integer cityId;
    //    4 Locality
    private Integer localityId;
    //    5 Community
    private Integer communityId;
    //    6 ClassType
    private Integer classTypeId;
    //    7 Class Schedule
    private Integer classScheduleId;
    //    8  Vendor
    private Integer vendorId;

    private Integer classDurationId;

    private String endDate = "";
    private String sortOrder = "";
    private String catalog = "";
    private String giftId = "";
    private String sortOrderCat = "";
    private String keywords = "";
    private String startDate = "";

    //  1
    public void setCategoryFilterMap(HashMap<String, Integer> categoryFilterMap) {
        if (categoryFilterMap != null) {
            this.categoryFilterMap = categoryFilterMap;
        } else {
            this.categoryFilterMap = new HashMap<>();
        }
        this.categoryId = getId(this.categoryFilterMap);

    }

    //  2
    public void setSegmentsFilterMap(HashMap<String, Integer> segmentsFilterMap) {
        if (segmentsFilterMap != null) {
            this.segmentsFilterMap = segmentsFilterMap;
        } else {
            this.segmentsFilterMap = new HashMap<>();
        }
        this.segmentId = getId(this.segmentsFilterMap);
    }

    //  3
    public void setCityFilterMap(HashMap<String, Integer> cityFilterMap) {
        if (cityFilterMap != null) {
            this.cityFilterMap = cityFilterMap;
        } else {
            this.cityFilterMap = new HashMap<>();
        }
        this.cityId = getId(this.cityFilterMap);
    }

    //  4
    public void setLocalityFilterMap(HashMap<String, Integer> localityFilterMap) {
        if (localityFilterMap != null) {
            this.localityFilterMap = localityFilterMap;
        } else {
            this.localityFilterMap = new HashMap<>();
        }
        this.localityId = getId(this.localityFilterMap);
    }

    //  5
    public void setCommunityFilterMap(HashMap<String, Integer> communityFilterMap) {
        if (communityFilterMap != null) {
            this.communityFilterMap = communityFilterMap;
        } else {
            this.communityFilterMap = new HashMap<>();
        }
        this.communityId = getId(this.communityFilterMap);
    }

    //  6
    public void setClassTypeFilterMap(HashMap<String, Integer> classTypeFilterMap) {
        if (classTypeFilterMap != null)
            this.classTypeFilterMap = classTypeFilterMap;
        else this.classTypeFilterMap = new HashMap<>();
        this.classTypeId = getId(this.classTypeFilterMap);
    }

    //  7
    public void setClassScheduleFilterMap(HashMap<String, Integer> classScheduleFilterMap) {
        if (classScheduleFilterMap != null) {
            this.classScheduleFilterMap = classScheduleFilterMap;
        } else this.classScheduleFilterMap = new HashMap<>();
        this.classScheduleId = getId(this.classScheduleFilterMap);
    }

    //  8
    public void setVendorFilterMap(HashMap<String, Integer> vendorFilterMap) {
        if (vendorFilterMap != null)
            this.vendorFilterMap = vendorFilterMap;
        else this.vendorFilterMap = new HashMap<>();
        this.vendorId = getId(this.vendorFilterMap);
    }

    // 9
    public void setClassDurationFilterMap(HashMap<String, Integer> classDurationFilterMap) {
        if (this.classDurationFilterMap != null)
            this.classDurationFilterMap = classDurationFilterMap;
        else this.classDurationFilterMap = new HashMap<>();
        this.classDurationId = getId(this.classDurationFilterMap);
    }

    public void setEndDate(String endDate) {
        if (endDate == null || "YYYY-MM-DD".equalsIgnoreCase(endDate))
            this.endDate = "";
        else this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        if (startDate == null || "YYYY-MM-DD".equalsIgnoreCase(startDate))
            this.startDate = "";
        else this.startDate = startDate;
    }


    public void setSortOrder(String sortOrder) {
        if (sortOrder != null)
            this.sortOrder = sortOrder;
        else this.sortOrder = "";
    }

    public void setCatalog(String catalog) {
        if (catalog != null)
            this.catalog = catalog;
        else this.catalog = "";
    }

    public void setGiftId(String giftId) {
        if (giftId != null)
            this.giftId = giftId;
        else this.giftId = "";
    }

    public void setSortOrderCat(String sortOrderCat) {
        if (sortOrderCat != null)
            this.sortOrderCat = sortOrderCat;
        else this.sortOrderCat = "";
    }

    public void setKeywords(String keywords) {
        if (keywords != null)
            this.keywords = keywords;
        else this.keywords = "";
    }


    //  1
    public HashMap<String, Integer> getCategoryFilterMap() {
        if (categoryFilterMap != null)
            return categoryFilterMap;
        else return new HashMap<>();
    }

    //  2
    public HashMap<String, Integer> getSegmentsFilterMap() {
        if (segmentsFilterMap != null)
            return segmentsFilterMap;
        else return new HashMap<>();
    }

    //  3
    public HashMap<String, Integer> getCityFilterMap() {
        if (cityFilterMap != null)
            return cityFilterMap;
        else return new HashMap<>();
    }

    //  4
    public HashMap<String, Integer> getLocalityFilterMap() {
        if (localityFilterMap != null)
            return localityFilterMap;
        else return new HashMap<>();
    }

    //  5
    public HashMap<String, Integer> getCommunityFilterMap() {
        if (communityFilterMap != null)
            return communityFilterMap;
        else return new HashMap<>();
    }

    //  6
    public HashMap<String, Integer> getClassTypeFilterMap() {
        if (classTypeFilterMap != null)
            return classTypeFilterMap;
        else return new HashMap<>();
    }

    //  7
    public HashMap<String, Integer> getClassScheduleFilterMap() {
        if (classScheduleFilterMap != null)
            return classScheduleFilterMap;
        else return new HashMap<>();
    }

    //  8
    public HashMap<String, Integer> getVendorFilterMap() {
        if (vendorFilterMap != null)
            return vendorFilterMap;
        else return new HashMap<>();
    }

    //  9
    public HashMap<String, Integer> getClassDurationFilterMap() {
        if (classDurationFilterMap != null)
            return classDurationFilterMap;
        else return new HashMap<>();
    }


    //  1
    public void setCategoryId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.categoryFilterMap.put(name, id);
        else
            this.categoryFilterMap = new HashMap<>();
        this.categoryId = id;


    }

    //  2
    public void setSegmentId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.segmentsFilterMap.put(name, id);
        else this.segmentsFilterMap = new HashMap<>();
        this.segmentId = id;
    }

    //  3
    public void setCityId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.cityFilterMap.put(name, id);
        else
            this.cityFilterMap = new HashMap<>();
        this.cityId = id;
    }

    //  4
    public void setLocalityId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.localityFilterMap.put(name, id);
        else this.localityFilterMap = new HashMap<>();
        this.localityId = id;
    }

    //  5
    public void setCommunityId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.communityFilterMap.put(name, id);
        else this.communityFilterMap = new HashMap<>();
        this.communityId = id;
    }

    //  6
    public void setClassTypeId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.classTypeFilterMap.put(name, id);
        else this.classTypeFilterMap = new HashMap<>();
        this.classTypeId = id;
    }

    //  7
    public void setClassScheduleId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.classScheduleFilterMap.put(name, id);
        else this.classScheduleFilterMap = new HashMap<>();
        this.classScheduleId = id;
    }

    //  8
    public void setVendorId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.vendorFilterMap.put(name, id);
        else this.vendorFilterMap = new HashMap<>();
        this.vendorId = id;
    }

    //  9
    public void setClassDurationId(String name, Integer id) {
        if (id != null && !isEmpty(name))
            this.classDurationFilterMap.put(name, id);
        else this.classDurationFilterMap = new HashMap<>();
        this.classDurationId = id;
    }

    //  1
    public Integer getCategoryId() {
        return categoryId;
    }

    //  2
    public Integer getSegmentId() {
        return segmentId;
    }

    //  3
    public Integer getCityId() {
        return cityId;
    }

    //  4
    public Integer getLocalityId() {
        return localityId;
    }

    //  5
    public Integer getClassScheduleId() {
        return classScheduleId;
    }

    //  6
    public Integer getCommunityId() {
        return communityId;
    }

    //  7
    public Integer getClassTypeId() {
        return classTypeId;
    }

    //  8
    public Integer getVendorId() {
        return vendorId;
    }

    //  9
    public Integer getClassDurationId() {
        return classDurationId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }


    public void setFilterData(FilterData data) {
//       1
        if (data.categoryFilterMap != null)
            this.categoryFilterMap = data.categoryFilterMap;
        else this.categoryFilterMap = new HashMap<>();
        this.categoryId = data.categoryId;
//       2
        if (data.segmentsFilterMap != null)
            this.segmentsFilterMap = data.segmentsFilterMap;
        else this.segmentsFilterMap = new HashMap<>();
        this.segmentId = data.segmentId;
//      3
        if (data.cityFilterMap != null)
            this.cityFilterMap = data.cityFilterMap;
        else this.cityFilterMap = new HashMap<>();
        this.cityId = data.cityId;
//      4
        if (data.localityFilterMap != null)
            this.localityFilterMap = data.localityFilterMap;
        else this.localityFilterMap = new HashMap<>();
        this.localityId = data.localityId;
//      5
        if (data.communityFilterMap != null)
            this.communityFilterMap = data.communityFilterMap;
        else this.communityFilterMap = new HashMap<>();
        this.communityId = data.communityId;
//      6
        if (data.classTypeFilterMap != null)
            this.classTypeFilterMap = data.classTypeFilterMap;
        else this.classTypeFilterMap = new HashMap<>();
        this.classTypeId = data.classTypeId;
//      7
        if (data.classScheduleFilterMap != null)
            this.classScheduleFilterMap = data.classScheduleFilterMap;
        else this.classScheduleFilterMap = new HashMap<>();
        this.classScheduleId = data.classScheduleId;
//      8
        if (data.vendorFilterMap != null)
            this.vendorFilterMap = data.vendorFilterMap;
        else this.vendorFilterMap = new HashMap<>();
        this.vendorId = data.vendorId;
//       9
        if (data.classDurationFilterMap != null)
            this.classDurationFilterMap = data.classDurationFilterMap;
        else this.classDurationFilterMap = new HashMap<>();
        this.classDurationId = data.classDurationId;

        this.endDate = data.endDate;
        this.startDate = data.startDate;
        this.sortOrder = data.sortOrder;
        this.catalog = data.catalog;
        this.sortOrderCat = data.sortOrderCat;
        this.giftId = data.giftId;
        this.keywords = data.keywords;
    }

    private Integer getId(@NonNull HashMap<String, Integer> hashMap) {
        if (hashMap.values().iterator().hasNext())
            return hashMap.values().iterator().next();
        else return null;
    }

    public GeneralFilterReq getFilterReq() {
        return new GeneralFilterReq(new GeneralFilterReq.Snippet("", keywords + "", startDate + "", endDate + "", getCategoryId() == null ? "" : getCategoryId().toString(), getSegmentId() == null ? "" : getSegmentId() + "", getClassTypeId() == null ? "" : getClassTypeId() + "", getCommunityId() == null ? "" : getCommunityId() + "", getClassScheduleId() == null ? "" : getClassScheduleId() + "", getClassDurationId() == null ? "" : getClassDurationId().toString(), getVendorId() == null ? "" : getVendorId() + "", getCityId() == null ? "" : getCityId() + "", getLocalityId() == null ? "" : getLocalityId() + "", sortOrder + "", sortOrderCat + "", catalog + "", giftId + ""));
    }


}
