import React from 'react';
import styled from 'styled-components';
import {Link} from 'react-router-dom';
import UserPhoto from '../../components-ui/user-photo';
import {Colors} from "../../helpers/style-variables";

export const ProfileBlock = ({index, id, username, countGames, rated, rating, iconPath}) => (
  <Content to={`/players/${id}`}>
    <Index>{index}</Index>
    <Photo>
      <UserPhoto photo={iconPath}/>
    </Photo>
    <Username>{username}</Username>
    <Statistics>{countGames}</Statistics>
    <Statistics>{rated}</Statistics>
    <Statistics>{rating}</Statistics>
  </Content>
)

const Content = styled(Link)`
  display: flex;
	border-bottom: #efefef solid 1px;
	color: black;
	text-decoration: none;
	align-items: center;
	padding: 15px 20px;
	min-height: 80px;
	box-sizing: border-box;
	span {
	  margin-right: 30px;
	}
  &:hover {
    background-color: ${Colors.HOVER_COLOR};
  }
`;
const Photo = styled.div`
  max-width: 50px;
  min-width: 50px;
  max-height: 50px;
  min-height: 50px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 100%;
`;
const Template = styled.div`
  padding-left: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  box-sizing: border-box;
`;

const Index = styled.div`
  max-width: 50px;
  min-width: 50px;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const Username = styled(Template)`
  max-width: 280px;
  min-width: 280px;
`;

const Statistics = styled(Template)`
  max-width: 160px;
  min-width: 160px;
  text-align: right;
`;

