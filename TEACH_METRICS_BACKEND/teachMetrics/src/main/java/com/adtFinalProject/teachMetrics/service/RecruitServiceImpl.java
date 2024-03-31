package com.adtFinalProject.teachMetrics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.adtFinalProject.teachMetrics.model.CandidatesModel;
import com.adtFinalProject.teachMetrics.model.ResultsModel;
import com.adtFinalProject.teachMetrics.model.SelectModel;
import com.adtFinalProject.teachMetrics.model.UserFilesModel;
import com.adtFinalProject.teachMetrics.model.profileModel;
import com.adtFinalProject.teachMetrics.model.userModel;

@Service
public class RecruitServiceImpl implements RecruitService {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	@Autowired
    private DataAccessBase dataAccessBase;

	public HashMap<String, Object> fetchCandidates() {
		HashMap<String, Object> response = new HashMap<String, Object>(); 
		List<CandidatesModel> candidatesList = new ArrayList<CandidatesModel>();
		int index = 0;
		
		MongoTemplate mongoTemplate = dataAccessBase.createMongoTemplate();
		
		Query query = new Query(Criteria.where("accVerified").is("A"));
    	List<userModel> approvedList = mongoTemplate.find(query, userModel.class);
		
    	for(int i = 0; i < approvedList.size(); i++) {
    		Query pQuery = new Query(Criteria.where("accId").is(approvedList.get(i).getAccId()));
        	List<profileModel> cProfileList = mongoTemplate.find(pQuery, profileModel.class);
        	
        	if(!cProfileList.isEmpty()) {
        		Query rQuery = new Query(Criteria.where("accId").is(approvedList.get(i).getAccId()));
        		List<ResultsModel> rResult = mongoTemplate.find(rQuery, ResultsModel.class);
        		if(!rResult.isEmpty() && "N".equals(cProfileList.get(0).getRecruited())) {
        			CandidatesModel cm = new CandidatesModel();
        			cm.setAccId(cProfileList.get(0).getAccId());
        			cm.setCandidateName(cProfileList.get(0).getlName() + ", " + cProfileList.get(0).getfName());
        			cm.setAbout(null);
        			cm.setExp(cProfileList.get(0).getExp());
        			cm.setContact(cProfileList.get(0).getContact());
        			cm.setAddress(cProfileList.get(0).getAddress());
        			cm.setPostalCode(cProfileList.get(0).getPostalCode());
        			cm.setEmailId(cProfileList.get(0).getEmailId());
        			cm.setScore(rResult.get(0).getScore());
        			cm.setPercentage(rResult.get(0).getPercentage());
        			candidatesList.add(index, cm);
        			index++;
        		}
        	}
    	}
    	
    	if(!candidatesList.isEmpty()) {
    		for(int i = 0; i < candidatesList.size(); i++) {
    			String accId = Integer.toString(candidatesList.get(i).getAccId());
    		
    			Query fquery = new Query(Criteria.where("accId").is(accId));	
    			List<UserFilesModel> fRes = mongoTemplate.find(fquery, UserFilesModel.class);
    			
    			if(!fRes.isEmpty()) {
    				candidatesList.get(i).setDocs(fRes);
    			}
    		}
    		response.put("candidatesList", candidatesList);
    	}
    	
		return response;
	}
	
	public HashMap<String, Object> selectCandidate(SelectModel sm) {
		HashMap<String, Object> response = new HashMap<String, Object>(); 
		MongoTemplate mongoTemplate = dataAccessBase.createMongoTemplate();
		
		Query query = new Query(Criteria.where("accId").is(sm.getAccId()));
    	List<userModel> result = mongoTemplate.find(query, userModel.class);
    	
    	if(!result.isEmpty()) {
    		Update update = new Update().set("recruited", "Y");
    		
    		mongoTemplate.updateFirst(query, update, profileModel.class);
    		
    		Query uQuery = new Query(Criteria.where("accId").is(sm.getAccId()));
        	List<profileModel> updatedResult = mongoTemplate.find(query, profileModel.class);
        	
        	if("Y".equalsIgnoreCase(updatedResult.get(0).getRecruited())) {
        		response.put("responseMessage", "Success");
        		response.put("responseCode", 1000);
        	} else {
        		response.put("responseMessage", "Error");
        		response.put("responseCode", 9999);
        	}
    	} else {
    		response.put("responseMessage", "Error");
    		response.put("responseCode", 9999);
    	}
		
		return response;
	}
	
	public HashMap<String, Object> fetchUserStatus(String emailId) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		
		Query query = new Query(Criteria.where("emailId").is(emailId));
		List<userModel> userList = mongoTemplate.find(query, userModel.class);
		
		if(!userList.isEmpty() && "T".equalsIgnoreCase(userList.get(0).getUserType())) {
			Query rquery = new Query(Criteria.where("accId").is(userList.get(0).getAccId()));
			List<ResultsModel> resultList = mongoTemplate.find(rquery, ResultsModel.class);
			
			if(!resultList.isEmpty()) {
				response.put("responseMessage", "Assessment Taken");
        		response.put("responseCode", 1000);
			} else {
				response.put("responseMessage", "Assessment Not Taken");
        		response.put("responseCode", 2000);
			}
		} else {
			response.put("responseMessage", "User Not Found");
    		response.put("responseCode", 9999);
		}
		
		return response;
	}
}
