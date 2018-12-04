import React from 'react';
import styled from 'styled-components';
import {Link} from 'react-router-dom';
import {MainMenuLink} from "../../components-ui/buttons/main-menu-link";
import GameRegistration from '../game-registration';

import UserPhoto from '../../components-ui/user-photo';
import {Colors} from '../../helpers/style-variables';

import MenuIco from '@material-ui/icons/Menu';
import CloseIco from '@material-ui/icons/Close';
import ExitToAppIco from '@material-ui/icons/ExitToApp';

class MobileMenu extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    }
  }

  onMenuOpen = () => this.setState({isOpen: !this.state.isOpen});

  handleOnBackgroundClick = (e) => {
    if (e.target.classList.contains(Content.styledComponentId)) {
      this.setState({isOpen: false})
    }
  }

  render() {
    const {iconPath, username, id} = this.props;
    return(
      <Content onClick={this.handleOnBackgroundClick}>
        <Title to="/">Zen<span>Kicker</span></Title>
        <MenuButton onClick={this.onMenuOpen}>{this.state.isOpen ?  <CloseIco /> : <MenuIco />}</MenuButton>
        {
          this.state.isOpen &&
          <TopBar>
            <UserSection>
              <User to={`/players/${id}`}>
                <Photo>
                  <UserPhoto photo={iconPath}/>
                </Photo>
                <Username>{username}</Username>
              </User>
              <a href="/logout"><LogOutIco/></a>
            </UserSection>
            <GameRegistration />
            <Navigation>
              <MainMenuLink link="/players">Players</MainMenuLink>
              <MainMenuLink link="/games">Games</MainMenuLink>
            </Navigation>
          </TopBar>
        }
      </Content>
    )
  }
}

export default MobileMenu;

const Content = styled.section`
	width: 100%;
	height: 50px;
	min-height: 50px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	position: fixed;
	background-color: ${Colors.THEME_COLOR};
	z-index: 10;
	box-sizing: border-box;
	padding: 0 20px;
`;

const Title = styled(Link)`
	width: max-content;
	font-size: 1.5em;
	display: block;
	color: ${Colors.MAIN_COLOR};
	text-decoration: none;
	&>span {
		color: black;
	}
`;

const MenuButton = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;
`;

const TopBar = styled.div`
	width: 100%;
	display: flex;
	flex-direction: column;
	position: fixed;
	box-sizing: border-box;
	align-items: center;
	top: 50px;
	left: 0;
	right: 0;
	padding: 20px;
	height: calc(100vh - 50px);
	background-color: ${Colors.THEME_COLOR};
`;

const Navigation = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const UserSection = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
`;

const User = styled(Link)`
  display: flex;
  width: 100%;
  align-items: center;
  color: black;
  text-decoration: none;
	&:hover {
		text-decoration: underline;
	}
`;

const Photo = styled.div`
  display: flex;
  align-items: center;
  overflow: hidden;
  max-width: 40px;
  min-width: 40px;
  max-height: 40px;
  min-height: 40px;
  justify-content: center;
  border-radius: 100%;
`;

const Username = styled.div`
  overflow: hidden;
  text-overflow: ellipsis;
	margin: 0 20px;
`;

const LogOutIco = styled(ExitToAppIco)`
  color: #000;
`;
