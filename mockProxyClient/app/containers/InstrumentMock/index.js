import React from 'react';
import axios from 'axios'
import _ from 'underscore'
import {envEntries} from 'components/env'
import DefaultResponse from 'components/DefaultResponse'
import MockScenarioList from 'components/MockScenarioList'
import ProxyMode from './proxy'

export default class InstrumentMock extends React.PureComponent {

constructor(props){
  super(props);
    this.state = {
      message:"",
      searchApi:"",
      searchResponse:[],
      mockResponse:{},
      importResponse: [],
      searchResponseIsValid: false,
      importResponsePresent: false,
      importMockFile: new FormData()
    };
    this.searchMocks = this.searchMocks.bind(this);
    this.selectApi = this.selectApi.bind(this);
    this.updateImportMockInState = this.updateImportMockInState.bind(this);
    this.clearSlate = this.clearSlate.bind(this);
    this.showMocks = this.showMocks.bind(this);
    this.toggleMockState = this.toggleMockState.bind(this);
    this.exportMocks=this.exportMocks.bind(this);
    this.exportMultipleMocks = this.exportMultipleMocks.bind(this);
    this.addToMockExportIndex = this.addToMockExportIndex.bind(this);
    this.viewMocksFromImportFile = this.viewMocksFromImportFile.bind(this);
    this.getMockScenarioState = this.getMockScenarioState.bind(this);

    this.showImportMockDefaultPanel = this.showImportMockDefaultPanel.bind(this);
    this.toggleImportState = this.toggleImportState.bind(this);
    this.importMultipleMocks = this.importMultipleMocks.bind(this);
    this.addToMockImportIndex = this.addToMockImportIndex.bind(this);
    this.showImportMocks = this.showImportMocks.bind(this)
}


getMockScenarioState(state){
console.log(state);
}

viewMocksFromImportFile(){

  var self= this;
  axios.post(envEntries.host + '/gateway/mocks/upload/view',
  self.state.importMockFile ,
  {headers:
    {
    'Content-Type': 'multipart/form-data', 'Accepts':'application/json'}
  })
.then(function (response) {
  console.log(response.data);

  var compositeMockResponseList = []
  _.each(response.data.listOfCompositeMockResponse, function(compositeMockResponse, index){
    compositeMockResponse.showDefaultPanel=false;
    compositeMockResponse.showImportMock=false;
    compositeMockResponse.showImportMockBlock=false;
    compositeMockResponse.index=index;
    compositeMockResponseList.push(compositeMockResponse);
  })

  self.setState({
    importResponse: compositeMockResponseList,
    importResponsePresent: true
  });
})
.catch(function (error) {
  self.setState({
    importResponse: [],
    message:"Import file is not valid or tampered, please use a different file",
    importResponsePresent: false
  });
});


}

addToMockExportIndex(e, id){
  var self= this;
  _.each(self.state.searchResponse, function(objectCache){
    if(objectCache.index == id){
      objectCache.exportStatus= e.target.checked;
    }
  });
  self.setState({
    message: ''
  })
}

exportMocks(id){
  var mockExportRequest = {};
  var self = this;

_.each(self.state.searchResponse, function(objectCache){
  if(objectCache.index == id){
    mockExportRequest.index= objectCache.index;
    mockExportRequest.api = objectCache.api;
    mockExportRequest.method = objectCache.method;
  }
});
    axios.post(envEntries.host +'/gateway/mocks/export/single', {
      api: "http://localhost:8080" + mockExportRequest.api,
      method :  mockExportRequest.method
    },
      {headers:
        {'contentType' : 'application/json', 'Accepts': 'application/json'}
      })
    .then(function (response) {
      var blob = new Blob([response.data], { encoding:"UTF-8",type:"text/plain;charset=UTF-8" });
      var filename='export_data.dat'  ;
      if (navigator.msSaveBlob) { // IE 10+
          navigator.msSaveBlob(blob, filename);
      } else {
          var link = document.createElement("a");
          if (link.download !== undefined) {
              var url = URL.createObjectURL(blob);
              link.setAttribute("href", url);
              link.setAttribute("download", filename);
              link.style.visibility = 'hidden';
              document.body.appendChild(link);
              link.click();
              document.body.removeChild(link);
          }
      }
    })
    .catch(function (error) {
      this.setState({
        message: 'Export mock is not successfull'
      })
    });

}

exportMultipleMocks(){

  var mockExportRequestList = [];
  var self = this;


  _.each(self.state.searchResponse, function(objectCache){
  if(objectCache.exportStatus == true){
    var mockExportRequest = {};
    mockExportRequest.index= objectCache.index;
    mockExportRequest.api = "http://localhost:8080" + objectCache.api;
    mockExportRequest.method = objectCache.method;
    mockExportRequestList.push(mockExportRequest);
  }
  });

  if(mockExportRequestList.length > 0){
        axios.post(envEntries.host +'/gateway/mocks/export/collection', mockExportRequestList,
          {headers:
            {'contentType' : 'application/json', 'Accepts': 'application/json'}
          })
        .then(function (response) {
          var blob = new Blob([response.data], { encoding:"UTF-8",type:"text/plain;charset=UTF-8" });
          var filename='export_data.dat'  ;
          if (navigator.msSaveBlob) { // IE 10+
              navigator.msSaveBlob(blob, filename);
          } else {
              var link = document.createElement("a");
              if (link.download !== undefined) {
                  var url = URL.createObjectURL(blob);
                  link.setAttribute("href", url);
                  link.setAttribute("download", filename);
                  link.style.visibility = 'hidden';
                  document.body.appendChild(link);
                  link.click();
                  document.body.removeChild(link);
              }
          }
        })
        .catch(function (error) {
          this.setState({
            message: 'Export mock is not successfull'
          })
        });
  }else{
    self.setState({
      message: 'Please select at least one check box to export list'
    })
  }


}

toggleMockState(id){
  console.log('here');
  var self= this;
  _.each(self.state.searchResponse, function(objectCache){
    if(objectCache.index == id){
        objectCache.showMockBlock =  !objectCache.showMockBlock;
    }
  });

  this.setState({
    searchResponse: self.state.searchResponse
  });
  self.forceUpdate();
}

showMocks(id){
    var mockViewRequest = {};
    var self = this;

  _.each(self.state.searchResponse, function(objectCache){
    if(objectCache.index == id){
      mockViewRequest.index= objectCache.index;
      mockViewRequest.api = objectCache.api;
      mockViewRequest.method = objectCache.method;
    }
  });
    axios.post(envEntries.host +'/gateway/mocks/view', {
      api: "http://localhost:8080"+ mockViewRequest.api,
      method :  mockViewRequest.method
    },
      {headers:
        {'contentType' : 'application/json', 'Accepts': 'application/json'}
      })
    .then(function (response) {
      _.each(self.state.searchResponse, function(objectCache){
        if(objectCache.index == id){
          objectCache.showMocks = true
          objectCache.showMockBlock=true;
        }
        });
      self.state.mockResponse[mockViewRequest.api+ '|' + mockViewRequest.method] = response.data;

      console.log(self.state.mockResponse);

      self.setState({
        searchResponse: self.state.searchResponse,
        mockResponse: self.state.mockResponse
      })

      self.forceUpdate();
    })
    .catch(function (error) {
      this.setState({
        message: 'Weird scenario, no mock exist for this one in server'
      })
    });
}

clearSlate(){
  this.setState({
    message:"",
    searchApi:"",
    searchResponse:{},
    mockResponse:{},
    importResponse: [],
    searchResponseIsValid: false,
    importMockFile: new FormData()
  });
}

updateImportMockInState(e){
    var formdata = this.state.importMockFile;
    var files = e.target.files[0];
    formdata.append('file',files);
    this.state.importMockFile=formdata;
    console.log(this.state);
}

updateState(index, state){

}

selectApi(event){
  if(event.target.value == ''){
    this.setState({
      message: 'Please select method and search api',
      searchApi: event.target.value
    });
  }else{
    this.setState({
      message: '',
      searchApi: event.target.value
    });
  }
}

searchMocks(){

  var self = this;
  self.setState({
    message: '',
    searchApi: self.state.searchApi,
    searchResponseIsValid: false
  });

  if(self.state.searchApi =='' || !(self.state.searchApi.length > 3)){
    this.setState({
      message: 'Please select search api, search api length should be at least 3 characters',
    })
  }else{
    axios.get(envEntries.host +'/gateway/mocks/search/'+ btoa(self.state.searchApi),
    {headers:
      {'contentType' : 'application/json', 'Accepts': 'application/json'}
    })
  .then(function (response) {
      console.log(response.data);
      var searchResponse = [];
      if(response.data.length > 0){

        _.each(response.data, function(item, index){
          var objectCache = {};
          objectCache.api=item.api;
          objectCache.method=item.method;
          objectCache.index=index;
          objectCache.showMocks=false;
          objectCache.showMockBlock=false;
          searchResponse.push(objectCache);
        });

        self.setState({
          message: '',
          searchResponse: searchResponse,
          searchResponseIsValid: true
        });

      }else{
        self.setState({
          message: 'Could not find any mocks with the search',
          searchResponse: response.data,
          searchResponseIsValid: false
        });
      }
  })
  .catch(function (error) {
    var errorMessge = "";
    if(error.response == undefined || error.response.data === undefined || error.response.data.description === undefined){
      errorMessge = 'Mocks not found for the search';
    }else{
      errorMessge = error.response.data.description;
    }
    self.setState({
            message: errorMessge,
            searchResponseIsValid: false
          });
  });
  }
}

toggleImportState(id){
  var self= this;
  _.each(self.state.importResponse, function(importCache){
    if(importCache.index == id){
        importCache.showImportMockBlock =  !importCache.showImportMockBlock;
    }
  });

  this.setState({
    importResponse: self.state.importResponse
  });
  self.forceUpdate();
}


importMultipleMocks(){
  //to be implemented
}

addToMockImportIndex(event, index){
  //to be implemented
}

showImportMockDefaultPanel(index){
  var self = this;
  _.each(self.state.importResponse, function(importCache){
    if(importCache.index==index){
      importCache.showDefaultPanel=true;
      importCache.showImportMockBlock=true;
    }
  });
  self.setState({
    importResponse: self.state.importResponse
  });
  self.forceUpdate();
  }


showImportMocks(defaultTemplateState, index){
  console.log('here111');
    var self = this;
  if(defaultTemplateState.defaultTemplatePresent){
  _.each(self.state.importResponse, function(importCache){
    if(importCache.index==index){
      importCache.showDefaultPanel=true;
      importCache.showImportMockBlock=true;
      importCache.showImportMock=true;
    }
  });
  self.setState({
    importResponse: self.state.importResponse

  });
  self.forceUpdate();
  }

}

render() {
  let message = this.state.message;
  var self = this;

  return (
      <div className="card">
      <div className="card-header" style={{cursor: 'pointer'}}>Instrument | Edit your mocks</div>
      <div className="card-body">

        <div className="row">
          <div className="col-sm-1">
            &nbsp;
          </div>
          <div className="col-sm-5">
            <input type="text" className="form-control form-control-sm"
              placeholder="http://localhost:8080/v1/demo/example" value={this.state.searchApi}
              onChange = {(e)=> this.selectApi(e)}/>
          </div>
          <div className="col-sm-3">
            <button type="submit" className="btn btn-sm btn-primary" onClick = {this.searchMocks}>Search</button>
          </div>

          <div className="col-sm-2">
              <div className="badge badge-success">
              <input type="file" id = "mockImport" accept=".dat" style={{fontSize: '70%', width: '100%'}}
                onChange={(e)=>this.updateImportMockInState(e)}/>
            </div>
            <div className="badge badge-primary" onClick={self.viewMocksFromImportFile}>
              View Mocks
            </div>
          </div>

          <div className="col-sm-1">
            <button type="submit" className="btn btn-sm btn-danger" onClick={this.clearSlate}>Clear Slate</button>
          </div>
        </div>

        <div className="row badge badge-danger">
          {this.state.message}
        </div>

          {
            this.state.searchResponseIsValid ?
            <React.Fragment>
              <div className="row alert alert-success">
                Search Response
              </div>

              <div className="row">
                  <div className="badge badge-primary" onClick={self.exportMultipleMocks}>Export Mock</div>
              </div>
              <div>
                  {
                    this.state.searchResponse.map((objectCache, index)=> (
                      <React.Fragment key={index}>
                      <div className="row alert-primary" style={{marginBottom: 5, marginTop: 5, cursor: 'pointer'}}
                        onClick={() => this.toggleMockState(objectCache.index)}>

                        <div className ="col-sm-1"><input type="checkbox" onChange={(e) => self.addToMockExportIndex(e, objectCache.index)}></input></div>
                        <div className="col-sm-1">{objectCache.method}</div>
                        <div className="col-sm-7">
                          <div className="badge badge-success">{objectCache.api}</div>
                        </div>
                        <div className="col-sm-1">
                        &nbsp;
                        </div>
                        <div className="col-sm-1">
                          <div className="badge badge-secondary" onClick={(e) => self.showMocks(objectCache.index)}>Show Mocks</div>
                        </div>
                        <div className="col-sm-1">
                          <div className="badge badge-secondary" onClick={() => self.exportMocks(objectCache.index)}>Export Mock</div>
                        </div>
                      </div>

                      {
                      objectCache.showMocks && objectCache.showMockBlock ?
                      <React.Fragment>

                        <DefaultResponse hideRefreshPanel ={true}
                          apiName={objectCache.api}
                          method ={objectCache.method}
                          defaultResponse={self.state.mockResponse[objectCache.api+'|'+objectCache.method].defaultResponse}
                          delayTime ={self.state.mockResponse[objectCache.api+'|'+objectCache.method].delayTime}
                          delayPercentage ={self.state.mockResponse[objectCache.api+'|'+objectCache.method].delayPercentage}
                          httpStatus = {self.state.mockResponse[objectCache.api+'|'+objectCache.method].httpStatus}
                          headers = {self.state.mockResponse[objectCache.api+'|'+objectCache.method].defaultHeaders}
                          defaultPanelId = {objectCache.api+'|'+objectCache.method}
                          >
                        </DefaultResponse>

                        <MockScenarioList api ={objectCache.api}
                          method = {objectCache.method}
                          loadMocksAsSave={false}
                          showRefreshLink={false}
                          loadMocksByDefault={true}
                          mockCriteriaList={self.state.mockResponse[objectCache.api+'|'+objectCache.method].mockCriteriaList}
                          >
                        </MockScenarioList>
                      </React.Fragment>
                      :
                      <div></div>
                      }
                    </React.Fragment>
                  ))

                }

              </div>


            </React.Fragment>
            :
            <div></div>
          }


          {
            self.state.importResponsePresent ?
            <React.Fragment>
              <div className="row alert alert-success">
                Import Response
              </div>

              <div className="row">
                  <div className="badge badge-primary" onClick={self.importMultipleMocks}>Import Mocks</div>
              </div>

              {
                this.state.importResponse.map((importCache, index)=> (
                  <React.Fragment key={index}>
                  <div className="row alert-primary" style={{marginBottom: 5, marginTop: 5}}>

                    <div className ="col-sm-1"><input type="checkbox"
                      onChange={(e) => self.addToMockImportIndex(e, importCache.index)}></input></div>
                    <div className="col-sm-1">{importCache.method}</div>
                    <div className="col-sm-9" onClick={() => this.toggleImportState(index)}
                      style={{cursor: 'pointer'}}>
                      <div className="badge badge-success" >{importCache.api}</div>
                    </div>
                    <div className="col-sm-1">
                      <div className="badge badge-secondary" onClick={(e) => self.showImportMockDefaultPanel(importCache.index)}>Show Mocks</div>
                    </div>
                  </div>

                    {importCache.showDefaultPanel && importCache.showImportMockBlock ?

                      <React.Fragment>

                        <DefaultResponse hideRefreshPanel ={true}
                          apiName={importCache.api}
                          method ={importCache.method}
                          defaultResponse={importCache.defaultResponse}
                          delayTime ={importCache.delayTime}
                          delayPercentage ={importCache.delayPercentage}
                          httpStatus = {importCache.httpStatus}
                          headers = {importCache.defaultHeaders}
                          defaultPanelId = {importCache.index}
                          childState={this.showImportMocks}
                          >
                        </DefaultResponse>

                        {
                          importCache.showImportMock ?
                              <MockScenarioList
                                api ={importCache.api}
                                method ={importCache.method}
                                loadMocksAsSave={true}
                                loadMocksByDefault={true}
                                showRefreshLink={false}
                                mockCriteriaList={importCache.mockCriteriaList}
                                >
                              </MockScenarioList>
                          :
                          <div></div>
                        }

                      </React.Fragment>
                    :
                        <div></div>
                    }

                </React.Fragment>
              ))
            }
          </React.Fragment>
        :
        <div></div>
      }

      <ProxyMode></ProxyMode>
    </div>
    </div>
  );
}

}
