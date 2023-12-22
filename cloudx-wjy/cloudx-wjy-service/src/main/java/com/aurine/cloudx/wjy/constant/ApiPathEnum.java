package com.aurine.cloudx.wjy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiPathEnum {
    GetTicket("/api/users/ticket", "GET"),
    GetAccessToken("/api/users/access_token","POST"),
    RefleshAccessTocke("/api/users/getTokenByCode","POST"),
    UploadExcept("/api/users/notice","GET"),
    SwitchProject("/api/users/switchProject","GET"),

    ProjectGetCityList("/api/projects/list","GET"),
    ProjectGetCompanyList("/api/projects/all","GET"),
    ProjectGetMyList("/api/projects/my","GET"),
    ProjectGetListByDept("/api/projects/getProjectList","POST"),
    ProjectGetTenementCommunityList("/api/projects/checkList","GET"),
    ProjectGetByPhone("/api/projects/getProjectListByPhone","GET"),
    ProjectSyncOrNew("/api/projects/sync_project","POST"),
    ProjectGetDeptByPage("/api/orgUnit/getOrgUnitList","GET"),
    ProjectGetAreaOrComany("/api/orgUnit/getAllOrgUnit","GET"),
    ProjectSave("/api/projects/bathSave","POST"),
    ProjectQueryByName("/api/projects/getProjectListByName","GET"),

    BuildGetList("/api/buildings/getApiBuildingList","GET"),
    BuildSave("/api/buildings/bathSave","POST"),
    BuildGetBashList("/api/buildings/batchList","GET"),

    RoomGetList("/api/rooms/list","GET"),
    RoomGetDetail("/api/rooms/getRoomDetailByID","GET"),
    RoomSave("/api/rooms/bathSave","POST"),
    RoomGetListDetail("/api/rooms/batchList","GET"),
    RoomGetModel("/api/rooms/getRoomModel","GET"),
    RoomApply("/api/rooms/applyRoom","GET"),
    RoomNew("/api/rooms/sSave","POST"),
    RoomBindCustomer("/api/roomCus/sSave","POST"),

    CustomerCheckout("/api/customers/joinout", "GET"),
    CustomerGetAllById("/api/customers/getMembers", "GET"),
    CustomerQueryByRoom("/api/customers/getRoomCusList", "GET"),
    CustomerBindRoomSync("/api/rooms/addroomcus", "POST"),
    CustomerUpdate("/api/customers/updateCustomer", "POST"),
    CustomerQueryByNameArea("/api/customers/list", "GET"),
    CustomerProprietorPost("/api/customerAudit/bathSave", "POST"),
    CustomerProprietorQueryPost("/api/customerAudit/listByRoom", "GET"),
    CustomerGetBatchList("/api/customers/batchList", "GET"),
    CustomerGetListInRoomBatch("/api/roomCus/batchList", "GET"),
    CustomerGetChanged("/api/customers/changes", "GET"),
    CustomerTalentAdd("/api/customers/addCustomer", "POST"),
    CustomerAndLinkAdd("/api/customers/pushCustomer", "POST"),
    CustomerStandardNew("/api/customers/sSave", "POST"),

    WorkerGetGroupMan("/api/sys/getGroupPermissionList","GET"),
    WorkerGetList("/api/employee/getEmployeeList","GET"),
    WorkerSyncSave("/api/employee/bathSave","POST"),
    WorkerDelete("/api/employee/deleteEmployeeByPhones","POST"),

    OrgSyncSave("/api/orgUnit/bathSave", "POST"),


    //AccountSaveHeader("/api/users/changeAvatar", "POST")
    ;

    private String path;
    private String method;
}