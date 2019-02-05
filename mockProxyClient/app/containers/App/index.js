import React from 'react';
//import { Switch, Route } from 'react-router-dom';
import {Redirect, Route,Switch} from 'react-router'
import {Router} from 'react-router-dom';
import GlobalStyle from '../../global-styles';

import HomePage from 'containers/HomePage/Loadable';
import SwaggerPage from 'containers/Swagger/Loadable';
import LandingLayout from  'containers/LandingLayout/Loadable';

export default function App(history) {
  return (
    <div>
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route exact path="/swagger" component={SwaggerPage} />
        <Route path="/landing" component={LandingLayout}/>
        <Route component={HomePage} />
      </Switch>
      <GlobalStyle />
    </div>
  );
}({history})
