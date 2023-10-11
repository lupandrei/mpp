package com.example.protobuffprotocol;

import com.example.model.*;

import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static MotoProtobufs.MotoRequest createLoginRequest(Admin admin){
        MotoProtobufs.User userDTO=MotoProtobufs.User.newBuilder().setId(admin.getID()).setPassword(admin.getPassword()).build();
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder().setType(MotoProtobufs.MotoRequest.Type.Login)
                .setUser(userDTO).build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createLogoutRequest(Admin admin){
        MotoProtobufs.User userDTO=MotoProtobufs.User.newBuilder().setId(admin.getID()).setPassword(admin.getPassword()).build();
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder().setType(MotoProtobufs.MotoRequest.Type.Logout)
                .setUser(userDTO).build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createGetAllEngineCapacitesRequest(){
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.GetAllEngineCapacities)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createGetRaceEntriesByEngineCapacityRequest(){
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.GetRaceEntriesByEngineCapacity)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createGetParticipantNameAndEngineCapacityRequest(String teamname){
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.GetParticipantNameAndEngineCapacity)
                .setTeamname(teamname)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createGetAllTeamNamesRequest(){
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.GetAllTeamNames)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createFindRaceByEngineCapacityRequest(int engineCapacity){
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.FindRaceByEngineCapacity)
                .setEnginecapacity(engineCapacity)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createGetLastParticipantRequest(){
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.GetLastParticipant)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createAddParticipantRequeset(Participant participant){
        MotoProtobufs.Participant participantDTO =MotoProtobufs.Participant.newBuilder().setId(participant.getID())
                .setFirstname(participant.getFirstName())
                .setLastname(participant.getLastName())
                .setTeamname(participant.getTeamName()).build();
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.AddParticipant)
                .setParticipant(participantDTO)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoRequest createAddEntryRequeset(Entry Entry){
        MotoProtobufs.Entry EntryDTO =MotoProtobufs.Entry.newBuilder().setIdrace(Entry.getIdRace())
                .setIdparticipant(Entry.getIdParticipant())
                .build();
        MotoProtobufs.MotoRequest request= MotoProtobufs.MotoRequest.newBuilder()
                .setType(MotoProtobufs.MotoRequest.Type.AddEntry)
                .setEntry(EntryDTO)
                .build();
        return request;
    }
    public static MotoProtobufs.MotoResponse createOkResponse(){
        MotoProtobufs.MotoResponse response=MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.Ok).build();
        return response;
    }

    public static MotoProtobufs.MotoResponse createErrorResponse(String text){
        MotoProtobufs.MotoResponse response=MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.Error)
                .setError(text).build();
        return response;
    }
    public static MotoProtobufs.MotoResponse createFriendLoggedOutResponse(Admin user){
        return MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.Ok).build();
    }
    public static MotoProtobufs.MotoResponse createGetRaceEntriesByEngineCapacityResponse(List<RaceDTO> races){
        MotoProtobufs.MotoResponse.Builder response = MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.GetRaceEntriesByEngineCapacity);
        for(RaceDTO race:races){
            MotoProtobufs.RaceDTO racedto = MotoProtobufs.RaceDTO.newBuilder()
                    .setEnginecapacity(race.getEngineCapacity())
                    .setRacename(race.getRaceName())
                    .setParticipants(race.getParticipants())
                    .build();
            response.addRaces(racedto);
        }
        return response.build();
    }
    public static MotoProtobufs.MotoResponse createGetParticipantNameAndEngineCapacityResponse(List<ParticipantDTO> participants){
        MotoProtobufs.MotoResponse.Builder response = MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.GetParticipantNameAndEngineCapacity);
        for(ParticipantDTO participant: participants){
            MotoProtobufs.ParticipantDTO participantDTO = MotoProtobufs.ParticipantDTO.newBuilder()
                    .setFirstname(participant.getFirstName())
                    .setLastname(participant.getLastName())
                    .setEnginecapacity(participant.getEngineCapacity())
                    .build();
            response.addParticipants(participantDTO);
        }
        return response.build();
    }
    public static MotoProtobufs.MotoResponse createGetAllTeamNamesResponse(List<String> teams){
        MotoProtobufs.MotoResponse.Builder response = MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.GetAllTeamNames);
        for(String team:teams){
            response.addTeamnames(team);
        }
        return response.build();
    }
    public static MotoProtobufs.MotoResponse createGetEngineCapacitiesResponse(List<Integer> engineCapacities){
        MotoProtobufs.MotoResponse.Builder response = MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.GetAllEngineCapacities);
        for(int capacity:engineCapacities){
            response.addEnginecapacities(capacity);
        }
        return response.build();
    }
    public static MotoProtobufs.MotoResponse createFindRaceByEngineCapacityResponse(Race race){
        MotoProtobufs.Race raceDTO =MotoProtobufs.Race.newBuilder()
                .setId(race.getID())
                .setName(race.getRaceName())
                .setEnginecapacity(race.getEngineCapacity())
                .build();
        MotoProtobufs.MotoResponse response =MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.FindRaceByEngineCapacity)
                .setRace(raceDTO)
                .build();
        return response;
    }
    public static MotoProtobufs.MotoResponse createGetLastParticipantResponse(Participant participant){
        MotoProtobufs.Participant participantDTO = MotoProtobufs.Participant.newBuilder()
                .setId(participant.getID())
                .setFirstname(participant.getFirstName())
                .setLastname(participant.getLastName())
                .setTeamname(participant.getTeamName())
                .build();
        MotoProtobufs.MotoResponse response = MotoProtobufs.MotoResponse.newBuilder()
                .setType(MotoProtobufs.MotoResponse.Type.GetLastParticipant)
                .setParticipant(participantDTO)
                .build();
        return response;
    }
    public static String getError(MotoProtobufs.MotoResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static Admin getUser(MotoProtobufs.MotoRequest request) {
        Admin admin=new Admin(request.getUser().getId(),request.getUser().getPassword());
        return admin;
    }

    public static List<RaceDTO> getRaceEntriesByEngineCapacity(MotoProtobufs.MotoResponse response) {
        List<RaceDTO> races = new ArrayList<>();
        for(int i = 0;i<response.getRacesCount();i++){
            MotoProtobufs.RaceDTO racedto =response.getRaces(i);
            RaceDTO race = new RaceDTO(racedto.getRacename(), racedto.getEnginecapacity(), racedto.getParticipants());
            races.add(race);
        }
        return races;
    }

    public static List<ParticipantDTO> getParticipantNameAndEngineCapacity(MotoProtobufs.MotoResponse response) {
        List<ParticipantDTO> participants = new ArrayList<>();
        for(int i = 0;i<response.getParticipantsCount();i++){
            MotoProtobufs.ParticipantDTO participantDTO =response.getParticipants(i);
            ParticipantDTO participant = new ParticipantDTO(participantDTO.getFirstname(),participantDTO.getLastname(),participantDTO.getEnginecapacity());
            participants.add(participant);
        }
        return participants;
    }

    public static List<String> getTeamNames(MotoProtobufs.MotoResponse response) {
        List<String> teamnames = new ArrayList<>();
        for(int i=0;i<response.getTeamnamesCount();i++){
            teamnames.add(response.getTeamnames(i));
        }
        return teamnames;
    }

    public static Race findRaceByEngineCapacity(MotoProtobufs.MotoResponse response) {
        Race race = new Race(response.getRace().getId(),response.getRace().getEnginecapacity(),
                response.getRace().getName());
        return race;
    }

    public static Participant getLastParticipant(MotoProtobufs.MotoResponse response) {
        Participant participant = new Participant(response.getParticipant().getId(),response.getParticipant().getFirstname(),
                response.getParticipant().getLastname(),response.getParticipant().getTeamname());
        return participant;
    }

    public static List<Integer> getEngineCapacities(MotoProtobufs.MotoResponse response) {
        List<Integer> capacities = new ArrayList<>();
        for(int i=0;i<response.getEnginecapacitiesCount();i++){
            capacities.add(response.getEnginecapacities(i));
        }
        return capacities;
    }
}
