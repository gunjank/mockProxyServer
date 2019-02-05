import React, {Component} from 'react';
import { Switch, Route } from 'react-router';
import {Link} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import { Nav, NavItem, Dropdown, DropdownItem, DropdownToggle, DropdownMenu, NavLink } from 'reactstrap';
import TopNavBar from 'components/TopNavBar'
import SwaggerMockPage from  '../SwaggerMock/Loadable';
import RecordMockPage from  '../RecordMock/Loadable';
import InstrumentMockPage from  '../InstrumentMock/Loadable';
import ProxySetupPage from '../Proxy/Loadable'
import './style.css'
import DirectionFooter from 'components/Directions'

export default class LandingLayout extends Component {
constructor(props){
  super(props);
}

render() {
  return (
    <div onChange={this.updateState}>
      <TopNavBar landing={true}></TopNavBar>

      <Switch>
      <Route path="/landing/swaggerMock" component ={SwaggerMockPage} />
      <Route path="/landing/recordMock" component ={RecordMockPage} />
      <Route path="/landing/instrumentMock" component ={InstrumentMockPage} />
      <Route path="/landing/proxySetup" component={ProxySetupPage} />
      </Switch>

      <DirectionFooter expand={true}></DirectionFooter>
</div>
  );
}
}
