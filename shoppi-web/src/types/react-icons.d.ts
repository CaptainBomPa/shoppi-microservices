import { ReactNode } from "react";
import { IconBaseProps } from "react-icons";

declare module "react-icons/lib" {
    export type IconType = (props: IconBaseProps) => ReactNode;
}
