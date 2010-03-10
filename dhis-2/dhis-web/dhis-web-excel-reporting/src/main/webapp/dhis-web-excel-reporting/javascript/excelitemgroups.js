// ========================================================================================================================
// EXCEL ITEM GROUP
// ========================================================================================================================
/*
*	Open Add Excel item group form
*/

function openAddExcelItemGroup() {

	enable( "name" );
	enable( "type" );
	$("#divExcelitemGroup").showAtCenter( true );
}

/*
*	Open Update Excel item group form
*/
function openUpdateExcelItemGroup( id ) {

	byId("id").value = id;
	
	var request = new Request();
	request.setResponseTypeXML( 'xmlObject' );
	request.setCallbackSuccess( openUpdateExcelItemGroupReceived );
	request.send( "getExcelItemGroup.action?id=" + id );
	
}

function openUpdateExcelItemGroupReceived( xmlObject ) {

	byId("name").value = xmlObject.getElementsByTagName('name')[0].firstChild.nodeValue;
	byId("type").value = xmlObject.getElementsByTagName('type')[0].firstChild.nodeValue;
	byId("periodType").value = xmlObject.getElementsByTagName('periodType')[0].firstChild.nodeValue;
		
	$("#divExcelitemGroup").showAtCenter( true );
	disable("name");
	disable("type");
}

/*
*	Validate Update Excel item group
*/
function validateExcelItemGroup() {
	
	var request = new Request();
	request.setResponseTypeXML( 'xmlObject' );
	request.setCallbackSuccess( validateExcelItemGroupReceived );
	
	var params = "name=" + byId('name').value;
		params += "&type=" + byId('type').value
	
	request.sendAsPost(params);
	request.send( "validateExcelItemGroup.action" );
	
}

function validateExcelItemGroupReceived( xmlObject ) {
	
	var type = xmlObject.getAttribute( 'type' );
	
	if ( type=='error' )
	{
		setMessage(xmlObject.firstChild.nodeValue);
	}
	else if ( type=='success' )
	{
		if ( mode == 'add' )
		{
			addExcelItemGroup();
		}
		else
		{
			updateExcelItemGroup();
		}	
	}
}

function addExcelItemGroup() {
	
	$.post("addExcelItemGroup.action",{
		name:$("#name").val(),	
		type:$("#type").val(),			
		periodTypeName:$("#periodType").val()
	}, function (data){
		window.location.reload();
	},'xml');
}

function addExcelItemGroupReceived() {

	window.location.reload();
}

function updateExcelItemGroup() {
	
	$.post("updateExcelItemGroup.action",{
		id:$("#id").val(),
		name:$("#name").val(),
		type:$("#type").val(),			
		periodTypeName:$("#periodType").val()
	}, function (data){
		window.location.reload();
	},'xml');
}

// --------------------------------------------------------------------
// DATA ELEMENT GROUP
// --------------------------------------------------------------------

/*
* 	Open Add Data Element Group Order 
*/
function openAddDataElementGroupOrder() {

	getALLDataElementGroups();
	document.forms['dataElementGroups'].action = "addDataElementGroupOrderForCategory.action";
}
/*
* 	Get Data Elements By Data Element Group
*/
function getDataElementsByGroup( id ) {
	
	if ( id == null )
	{
		return;
	}

	var url = "../dhis-web-commons-ajax/getDataElements.action?id=" + id;

	var request = new Request();
	request.setResponseTypeXML( 'datalement' );
	request.setCallbackSuccess( getDataElementsByGroupReceived );	
	request.send( url );	
}

function getDataElementsByGroupReceived( datalement ) {
	
	var dataElements = datalement.getElementsByTagName( "dataElement" );
	
	var listDataElement = document.getElementById('availableDataElements');
	
	listDataElement.options.length = 0;
	
	for ( var i = 0; i < dataElements.length; i++ )
    {
        var id = dataElements[ i ].getElementsByTagName( "id" )[0].firstChild.nodeValue;
        var name = dataElements[ i ].getElementsByTagName( "name" )[0].firstChild.nodeValue;
        var option = new Option( name, id );
		
		option.onmousemove  = function( e ) {
		
			showToolTip( e, this.text );
		}
		
		listDataElement.add( option, null );
    }
	
	var availableDataElements = document.getElementById('availableDataElements');
	var dataElementIds = document.getElementById('dataElementIds');
	
	for( var i = 0 ; i < availableDataElements.options.length ; i++ ) {
	
		for ( var j = 0 ; j < dataElementIds.options.length ; j++ ) {				
		
			if ( availableDataElements.options[i].value == dataElementIds.options[j].value ) {
			
				availableDataElements.options[i].style.display='none';				
			}
		}
	}
	
	$("#dataElementGroups").showAtCenter( true );	
}

function getALLDataElementGroups() {
	
	var request = new Request();
	request.setResponseTypeXML( 'datalement' );
	request.setCallbackSuccess( getALLDataElementGroupsReceived );	
	request.send( "getAllDataElementGroups.action" );	
	
}

function getALLDataElementGroupsReceived( data ) {
	
	var availableDataElementGroups = document.getElementById('availableDataElementGroups');
		
	availableDataElementGroups.options.length = 0;
		
	var dataElementGroups = data.getElementsByTagName('dataElementGroup');
		
	availableDataElementGroups.options.add(new Option("ALL", null));	
		
	for( var i = 0 ; i < dataElementGroups.length ; i++ ) {
			
		var id = dataElementGroups.item(i).getElementsByTagName('id')[0].firstChild.nodeValue;
		var name = dataElementGroups.item(i).getElementsByTagName('name')[0].firstChild.nodeValue;
		
		var option = new Option( name, id );
		availableDataElementGroups.add(option, null);
	}
	
	getDataElementsByGroup(byId("availableDataElementGroups").value);
}

