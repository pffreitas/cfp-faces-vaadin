package br.com.maisha.cfp.faces.ui.event

class RepositoryChangedEvent {

	private String repoName;
	
	private Map data = [:]
	
	public RepositoryChangedEvent(String pRepoName, Map pData = null){
		data = pData
		repoName = pRepoName
	}
	
	
}
