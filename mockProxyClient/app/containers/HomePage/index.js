import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import TopNavBar from 'components/TopNavBar'

/* eslint-disable react/prefer-stateless-function */
export default class HomePage extends React.PureComponent {


      render() {
          return (
            <React.Fragment>
            <TopNavBar landing={false}></TopNavBar>
              <video controls autoPlay loop id="mockVideo"
                style={{position: 'fixed', right: 0, top: 50, bottom: 0, minWidth: '100%' , maxHeight: '72%',
                  boxSizing: 'border-box' , background: 'linear-gradient(to top, black, grey)'}}>
                <source src="http://localhost:6500/videos/mock_server.mov"></source>
              </video>
            <div class="content" style={{ position: 'fixed', bottom: 0 ,background: 'rgba(0, 0, 0, 0.5)'
              , color: '#f1f1f1', width: '100%', padding: 20}}>
              <h3>Mock Proxy Server is a not just a simulator for HTTP-based APIs</h3>
              <p>Some might consider it a service virtualization tool or a mock server..
              It enables you to stay productive when an API you depend on doesn't exist or isn't complete. It supports testing of edge cases and failure modes that the real API won't reliably produce.
              And because it's fast it can reduce your build time from hours down to minutes.
            </p>

              <div className="row">
                <div className="col-sm-4">
                  Replay apis which are availble and create multiple scenarios based on request and response &nbsp;&nbsp;
                  <Link style={{ textDecoration: 'none', fontSize: '80%'}} to="/landing/recordMock">
                    <button type="submit" className="btn btn-sm btn-warning">
                    Mock it
                  </button>
                  </Link>
                </div>

                <div className="col-sm-4">
                  Get up and running quickly by capturing api from imported Swagger.&nbsp;&nbsp;
                  <Link style={{ textDecoration: 'none', fontSize: '80%'}} to="/landing/swaggerMock">
                      <button type="submit" className="btn btn-sm btn-warning">
                        Swagger it
                      </button>
                    </Link>
                </div>

                <div className="col-sm-4">
                  Edit import export mocks easily, save mocks on local machine for future use.&nbsp;&nbsp;
                  <Link style={{ textDecoration: 'none', fontSize: '80%'}} to="/landing/instrumentMock">
                    <button type="submit" className="btn btn-sm btn-warning">
                    Instrument it
                  </button>
                </Link>
                </div>

              </div>
            </div>
            </React.Fragment>
          );
      }
}
