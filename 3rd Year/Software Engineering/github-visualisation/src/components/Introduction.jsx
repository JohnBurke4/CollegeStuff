import React, { Component } from "react";
import {Jumbotron} from "react-bootstrap"

class Introduction extends Component {
    render() {
        return (
            <Jumbotron>
                <h1>Welcome to github commit analyser!</h1>
                <p>
                    By providing a repository, this webapp can analyse any commits to it. This can provide key insights,
                    such as the times people commit at and the most frequent commiters.
                </p>
            </Jumbotron>
        );
    }
}

export default Introduction;