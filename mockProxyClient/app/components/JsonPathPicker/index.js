//https://github.com/zenggo/react-json-path-picker/blob/master/src/react-json-path-picker.tsx

import * as React from 'react'
import './style.css'


export default class JsonPathPicker extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            choosen: null,
            index: this.props.index
        }
    }
    componentWillReceiveProps(nextp) {
        if (nextp.json !== this.props.json) { // string compare
            this.setState({
                choosen: null // reset choosen
            })
        }
        if (nextp.path !== undefined) {
            let nextPath;
            if (!nextp.path) { // '' | null
                nextPath = nextp.path
            } else {
                nextPath = nextp.path.replace(/\./g, ' .')
                nextPath = nextPath.replace(/\[/g, ' [')
            }
            this.setState({
                choosen: nextPath
            })
        }
    }
    shouldComponentUpdate(nextp, nexts) {
        if (nextp.json !== this.props.json) {
            return true
        } else if (nexts.choosen !== this.state.choosen) {
            return true
        } else {
            return false
        }
    }
    choose = (e) => {
        let target = e.target
        if (target.hasAttribute('data-pathkey')) {

            let pathKey = target.getAttribute('data-pathkey')
            let choosenPath

            if (target.hasAttribute('data-choosearr')) {

                choosenPath = this.state.choosen
                let tmp = choosenPath.split(' ')
                let idx = pathKey.split(' ').length
                tmp[idx] = '[*]'
                choosenPath = tmp.join(' ')

            } else {
                choosenPath = pathKey
            }

            this.setState({
                choosen: choosenPath
            }, ()=> {
                let pathText = this.state.choosen
                pathText = pathText.replace(/ /g, '')
                this.props.onChoose && this.props.onChoose(pathText, this.state.index)
            })

        }
    }
    render() {
        let jsonObj;
        try {
          jsonObj = JSON.parse(this.props.json);
        } catch (error) {
            console.log(error)
            return <div>Wrong json string input</div>
        }
        return (<div onClick={this.props.showOnly ? null : this.choose}>
            { this.props.showOnly
                ? this.json2Jsx_onlyForShow(jsonObj)
                : this.json2Jsx(this.state.choosen, jsonObj) }
        </div>)
    }

    /**
     * Check if a string represents a valid url
     * @return boolean
     */
    isUrl(str) {
        let regexp = /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        return regexp.test(str)
    }

   escape(str) {
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
    }


    /**
     * recursively generate jsxs by json data
     * @param choosenPath
     * @param jsonObj
     * @param isLast :is the last child or not
     * @param pathKey :now json path from root
     * @return reactElements
     */
    json2Jsx(choosenPath, jsonObj, isLast, pathKey) {

      if(isLast==null){
        isLast=true;
      }
      if(pathKey==null){
        pathKey='';
      }

        if (jsonObj === null) {
            return this.renderNull(choosenPath, isLast, pathKey)
        } else if (jsonObj === undefined) {
            return this.renderUndefined(choosenPath, isLast, pathKey)
        } else if (Array.isArray(jsonObj)) {
            return this.renderArray(choosenPath, isLast, pathKey, jsonObj)
        } else if (typeof jsonObj == 'string') {
            return this.renderString(choosenPath, isLast, pathKey, jsonObj)
        } else if (typeof jsonObj == 'number') {
            return this.renderNumber(choosenPath, isLast, pathKey, jsonObj)
        } else if (typeof jsonObj == 'boolean') {
            return this.renderBoolean(choosenPath, isLast, pathKey, jsonObj)
        } else if (typeof jsonObj == 'object') {
            return this.renderObject(choosenPath, isLast, pathKey, jsonObj)
        } else {
            return null
        }

    }

    // various types' render
    renderNull(choosenPath, isLast, pathKey)  {
        return (<span className="json-literal">
            <i data-pathkey={pathKey} className={this.getPickerStyle(this.getRelationship(choosenPath, pathKey))}>📋</i>
            <span>{'null'} {isLast?'':','}</span>
        </span>)
    }

    renderUndefined(choosenPath, isLast, pathKey){
        return (<span className="json-literal">
            <i data-pathkey={pathKey} className={this.getPickerStyle(this.getRelationship(choosenPath, pathKey))}>📋</i>
            <span>{'undefined'} {isLast?'':','}</span>
        </span>)
    }

    renderString(choosenPath, isLast, pathKey, str)  {
        str = escape(str)
        if (this.isUrl(str)) {
            return (<span>
                <i data-pathkey={pathKey} className={this.getPickerStyle(this.getRelationship(choosenPath, pathKey))}>📋</i>
                <a target="_blank" href={str} className="json-literal">
                    <span>"{str}" {isLast?'':','}</span>
                </a>
            </span>)
        } else {
            return (<span className="json-literal">
                <i data-pathkey={pathKey} className={this.getPickerStyle(this.getRelationship(choosenPath, pathKey))}>📋</i>
                <span>"{str}" {isLast?'':','}</span>
            </span>)
        }
    }

    renderNumber(choosenPath, isLast, pathKey, num)  {
        return (<span className="json-literal">
            <i data-pathkey={pathKey} className={this.getPickerStyle(this.getRelationship(choosenPath, pathKey))}>📋</i>
            <span>{num} {isLast?'':','}</span>
        </span>)
    }

    renderBoolean(choosenPath, isLast, pathKey, bool)  {
        return (<span className="json-literal">
            <i data-pathkey={pathKey} className={this.getPickerStyle(this.getRelationship(choosenPath, pathKey))}>📋</i>
            <span>{bool} {isLast?'':','}</span>
        </span>)
    }

    renderObject(choosenPath, isLast, pathKey, obj)  {
        let relation = this.getRelationship(choosenPath, pathKey)

        let keys = Object.keys(obj)
        let length = keys.length
        if (length > 0) {
            return (<div className={relation==1 ? "json-picked_tree" : ''}>
                <div>

                    <span>{'{'}</span>
                    <i data-pathkey={pathKey} className={this.getPickerStyle(relation)}>📋</i>
                </div>
                <ul className="json-dict">
                    {
                        keys.map((key, idx) => {
                            let nextPathKey = `${pathKey} .${key}`
                            return (<li key={nextPathKey}>
                                <span className="json-literal json-key">{key}</span>
                                <span> : </span>
                                { this.json2Jsx(choosenPath, obj[key], idx == length-1 ? true : false, nextPathKey) }
                            </li>)
                        })
                    }
                </ul>
                <div>{'}'} {isLast?'':','}</div>
            </div>)
        } else {
            return (<span>
                <i data-pathkey={pathKey} className={this.getPickerStyle(relation)}>📋</i>
                <span>{"{ }"} {isLast?'':','}</span>
            </span>)
        }
    }

    renderArray(choosenPath, isLast, pathKey, arr) {
        let relation = this.getRelationship(choosenPath, pathKey)

        let length = arr.length
        if (length > 0) {
            return (<div className={relation==1 ? "json-picked_tree" : ''}>
                <div>
                    { relation==2 ? <i data-pathkey={pathKey} data-choosearr="1" className={this.getPickArrStyle(choosenPath, pathKey)}>[✚]</i> : null }
                    <span>{'['}</span>
                    <i data-pathkey={pathKey} className={this.getPickerStyle(relation)}>📋</i>
                </div>
                <ol className="json-array">
                    {
                        arr.map((value, idx) => {
                            let nextPathKey = `${pathKey} [${idx}]`
                            return (<li key={nextPathKey}>
                                { this.json2Jsx(choosenPath, value, idx == length-1 ? true : false, nextPathKey) }
                            </li>)
                        })
                    }
                </ol>
                <div>{']'} {isLast?'':','}</div>
            </div>)
        } else {
            return (<span>
                <i data-pathkey={pathKey} className={this.getPickerStyle(relation)}>📋</i>
                <span>{"[ ]"} {isLast?'':','}</span>
            </span>)
        }
    }

    /**
     * get the relationship between now path and the choosenPath
     * 0 other
     * 1 self
     * 2 ancestor
     */
    getRelationship(choosenPath, path) {
        if (choosenPath === null) return 0

        let choosenAttrs = choosenPath.split(' ')
        choosenAttrs.shift()
        let choosenLen = choosenAttrs.length

        let nowAttrs = path.split(' ')
        nowAttrs.shift()
        let nowLen = nowAttrs.length

        if (nowLen > choosenLen) return 0

        for (let i=0; i<nowLen; i++) {
            let ok;

            if (nowAttrs[i] === choosenAttrs[i]) {
                ok = true
            } else if (nowAttrs[i][0] === '[' && choosenAttrs[i][0] === '[' && choosenAttrs[i][1] === '*') {
                ok = true
            } else {
                ok = false
            }

            if (!ok) return 0
        }

        return nowLen == choosenLen ? 1 : 2
    }

    /**
     * get picker's className, for ditinguishing picked or not or ancestor of picked entity
     */
    getPickerStyle(relation) {
        if (relation == 0) {
            return "json-pick_path"
        } else if (relation == 1) {
            return "json-pick_path json-picked"
        } else {
            return "json-pick_path json-pick_path_ancestor"
        }
    }

    getPickArrStyle(choosenPath, nowPath)  {
        let csp = choosenPath.split(' ')
        let np = nowPath.split(' ')
        if (csp[np.length] == '[*]') {
            return "json-pick_arr json-picked_arr"
        } else {
            return "json-pick_arr"
        }
    }


    /**
     * only for show json data
     */
    json2Jsx_onlyForShow(jsonObj, isLast)  {

          if(isLast==null){
            isLast=true;
          }

        if (jsonObj === null) {
            return (<span className="json-literal">
                <span>{'null'} {isLast?'':','}</span>
            </span>)
        } else if (jsonObj === undefined) {
            return (<span className="json-literal">
                <span>{'undefined'} {isLast?'':','}</span>
            </span>)
        } else if (Array.isArray(jsonObj)) {
            let arr = jsonObj
            let length = arr.length
            return (<div>
                <div>
                    <span>{'['}</span>
                </div>
                <ol className="json-array">
                    {
                        arr.map((value, idx) => {
                            return (<li key={idx}>
                                { this.json2Jsx_onlyForShow(value, idx == length-1 ? true : false) }
                            </li>)
                        })
                    }
                </ol>
                <div>{']'} {isLast?'':','}</div>
            </div>)
        } else if (typeof jsonObj == 'string') {
            let str = escape(jsonObj)
            if (this.isUrl(str)) {
                return (<span>
                    <a target="_blank" href={str} className="json-literal">
                        <span>"{str}" {isLast?'':','}</span>
                    </a>
                </span>)
            } else {
                return (<span className="json-literal">
                    <span>"{str}" {isLast?'':','}</span>
                </span>)
            }
        } else if (typeof jsonObj == 'number') {
            return (<span className="json-literal">
                <span>{jsonObj} {isLast?'':','}</span>
            </span>)
        } else if (typeof jsonObj == 'boolean') {
            return (<span className="json-literal">
                <span>{jsonObj} {isLast?'':','}</span>
            </span>)
        } else if (typeof jsonObj == 'object') {
            let keys = Object.keys(jsonObj)
            let length = keys.length
            if (length > 0) {
                return (<div>
                    <div>
                        <span>{'{'}</span>
                    </div>
                    <ul className="json-dict">
                        {
                            keys.map((key, idx) => {
                                return (<li key={idx}>
                                    <span className="json-literal json-key">{key}</span>
                                    <span> : </span>
                                    { this.json2Jsx_onlyForShow(jsonObj[key], idx == length-1 ? true : false) }
                                </li>)
                            })
                        }
                    </ul>
                    <div>{'}'} {isLast?'':','}</div>
                </div>)
            } else {
                return (<span>
                    <span>{"{ }"} {isLast?'':','}</span>
                </span>)
            }
        } else {
            return null
        }
    }

}
