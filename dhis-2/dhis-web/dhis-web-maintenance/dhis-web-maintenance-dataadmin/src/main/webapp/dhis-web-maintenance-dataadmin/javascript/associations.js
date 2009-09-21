
var numberOfSelects = 0;

function selectAllAtLevel( dataSetId )
{
	var list = document.getElementById( 'levelList' );
    
    var level = list.options[ list.selectedIndex ].value;
    
    window.location.href = 'selectLevel.action?level=' + level + '&dataSetId=' + dataSetId;
}

function unselectAllAtLevel( dataSetId )
{
	var list = document.getElementById( 'levelList' );
    
    var level = list.options[ list.selectedIndex ].value;
    
    window.location.href = 'unselectLevel.action?level=' + level + '&dataSetId=' + dataSetId;
}

function treeClicked()
{
    numberOfSelects++;
    
    setMessage( i18n_loading );

    parent.document.getElementById( "submitButton" ).disabled = true;
}

function saveDissable()
{
	parent.document.getElementById( "submitButton" ).disabled = true;
}

function saveEnable()
{
	parent.document.getElementById( "submitButton" ).disabled = false;
}

function selectCompleted( selectedUnits )
{
    numberOfSelects--;
    
    if ( numberOfSelects <= 0 )
    {
        hideMessage();
        
       parent.document.getElementById( "submitButton" ).disabled = false;
    }
}
