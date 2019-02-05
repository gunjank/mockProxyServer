import React from 'react';
import deepMerge from "deepmerge"
import SwaggerUI from "swagger-ui"
import EditorLayout from "./layout"
import "swagger-ui/dist/swagger-ui.css"
import "./style.css"
//import StandaloneLayout from "./standalone"

import EditorPlugin from "./plugins/editor"
import LocalStoragePlugin from "./plugins/local-storage"
import ValidateBasePlugin from "./plugins/validate-base"
import ValidateSemanticPlugin from "./plugins/validate-semantic"
//import ValidateJsonSchemaPlugin from "./plugins/validate-json-schema"
import EditorAutosuggestPlugin from "./plugins/editor-autosuggest"
import EditorAutosuggestSnippetsPlugin from "./plugins/editor-autosuggest-snippets"
import EditorAutosuggestKeywordsPlugin from "./plugins/editor-autosuggest-keywords"
import EditorAutosuggestOAS3KeywordsPlugin from "./plugins/editor-autosuggest-oas3-keywords"
import EditorAutosuggestRefsPlugin from "./plugins/editor-autosuggest-refs"
import PerformancePlugin from "./plugins/performance"
import JumpToPathPlugin from "./plugins/jump-to-path"
import SplitPaneModePlugin from "./plugins/split-pane-mode"
import ASTPlugin from "./plugins/ast"
import TopNavBar from 'components/TopNavBar'
import 'bootstrap/dist/css/bootstrap.css';
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    Container,
    Row,
    Col,
    Jumbotron,
    Button,
    Card, CardImg, CardText, CardBody,
  CardTitle, CardSubtitle
} from 'reactstrap';

const plugins = {
   EditorPlugin,
   ValidateBasePlugin,
   ValidateSemanticPlugin,
   //ValidateJsonSchemaPlugin,
   LocalStoragePlugin,
   EditorAutosuggestPlugin,
   EditorAutosuggestSnippetsPlugin,
   EditorAutosuggestKeywordsPlugin,
   EditorAutosuggestRefsPlugin,
   EditorAutosuggestOAS3KeywordsPlugin,
   PerformancePlugin,
   JumpToPathPlugin,
   SplitPaneModePlugin,
   ASTPlugin,
 }

 const defaults = {
   dom_id: "#swagger-editor", // eslint-disable-line camelcase, we have this prop for legacy reasons.
   layout: "EditorLayout",
   presets: [
     SwaggerUI.presets.apis
   ],
   plugins: Object.values(plugins),
   components: {
     EditorLayout
   },
   showExtensions: true,
   swagger2GeneratorUrl: "https://generator.swagger.io/api/swagger.json",
   oas3GeneratorUrl: "https://generator3.swagger.io/api/swagger.json"
 };

export default class SwaggerEditor extends React.PureComponent {

componentDidMount(){
  let options={
    dom_id: '#swagger-editor',
    layout: 'EditorLayout',
    presets: [
      SwaggerUI.presets.apis
    ],
    plugins: Object.values(plugins),
    showExtensions: true,
    swagger2GeneratorUrl: "https://generator.swagger.io/api/swagger.json",
    oas3GeneratorUrl: "https://generator3.swagger.io/api/swagger.json"
  };
  window.editor = options
  let mergedOptions = deepMerge(defaults, options)
//let mergedOptions = defaults;

  mergedOptions.presets = defaults.presets.concat(options.presets || [])
  mergedOptions.plugins = defaults.plugins.concat(options.plugins || [])
  SwaggerUI(mergedOptions);

}

  render() {
    return (
      <div>
        <TopNavBar></TopNavBar>
        <div id="swagger-editor" style={{fontSize: '65%'}}>Swagger Editor</div>
        </div>
    );
  }
}
